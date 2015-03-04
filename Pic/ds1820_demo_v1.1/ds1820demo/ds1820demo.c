/*** FILEHEADER ****************************************************************
 *
 *    FILENAME:    ds1820demo.c
 *    DATE:        26.02.2005
 *    AUTHOR:      (c) Christian Stadler
 *
 *    DESCRIPTION: Demo programm for DS1820 temperature sensor driver.
 *
 ******************************************************************************/

/*** HISTORY OF CHANGE *********************************************************
 *
 *    $Log: /pic/ds1820demo/ds1820demo.c $
 * 
 * 8     13.11.10 20:30 Stadler
 * - ported to Microchip C18 compiler
 * 
 * 7     6.11.10 10:26 Stadler
 * - 1st official demo
 * 
 * 6     2.11.10 21:11 Stadler
 * - removed LCD output
 * 
 * 5     2.11.10 20:33 Stadler
 * - update to new DS1820 driver interface
 * 
 * 4     13.03.05 12:34 Stadler
 * - added degree character for temperature output on LCD 
 *
 * 3     13.03.05 11:58 Stadler
 * - adaptions due to LCD driver changes
 *
 * 2     12.03.05 11:24 Stadler
 * - added EEPROM write function
 * - added "Search ROM Algorithm" to control multiple devices
 *
 * 1     26.02.05 18:38 Stadler
 * Initial creation of DS1820 demo program.
 *
 ******************************************************************************/

#include "ds1820demo.h"
#include "..\_inc\types.h"


/* --- configure DS1820 temparture sensor pin --- */
#if defined(__18CXX)
#define DS1820_DATAPIN      PORTBbits.RB3
#define output_low(pin)     TRISBbits.TRISB3 = 0;(PORTBbits.RB3 = 0)
#define output_high(pin)    TRISBbits.TRISB3 = 0;(PORTBbits.RB3 = 1)
#define input(pin)          input_func()
bool input_func(void)
{
    TRISBbits.TRISB3 = 1;
    return (PORTBbits.RB3);
}
#endif

#if (defined(__PCB__) || defined(__PCH__) || defined(__PCM__))
#define DS1820_DATAPIN  PIN_B3
#endif

#include "..\_drv\ds1820.h"



/*******************************************************************************
 * FUNCTION:   initialize
 * PURPOSE:    Initializes PIC.
 ******************************************************************************/
void initialize()
{
    /* C18 compiler specific */
    #if defined(__18CXX)
    ADCON1 |= 0x0F;
    
    /* set direction registers */
    LATC = 0xFF;
    TRISC = 0x00;
    TRISD = 0x00;
    LATD = 0x00;

    /* baud rate formula for BRGH = 0: */
    /* BaudRate = FOSC/(64 (X + 1)) */
    /* => BaudRate (64 (X + 1)) = FOSC */
    /* => 64 (X + 1) = FOSC / BaudRate */
    /* => X = (FOSC / BaudRate) / 64 - 1 */
    /* => X = SPBRG = /32000000 / 19200) / 64 - 1 = 25.04166 => 25 */
    OpenUSART(USART_TX_INT_OFF &    /* disables TX interrupt */
              USART_RX_INT_OFF &    /* disables RX interrupt */
              USART_ASYNCH_MODE &   /* sets it in asynchronous mode */
              USART_EIGHT_BIT &     /* sets to use 8-bit data mode */
              USART_CONT_RX &       /* sets the port in continues receive mode */
              USART_BRGH_LOW,       /* uses the low speed Baud rate formula */
              25);                  /* SPBRG, sets Baud to 19200 BPS */
    #endif /* #if defined(__18CXX) */
    

    /* CCS compiler specific */    
    #if (defined(__PCB__) || defined(__PCH__) || defined(__PCM__))
    setup_adc_ports(NO_ANALOGS);
    setup_adc(ADC_OFF);
    setup_spi(FALSE);
    setup_wdt(WDT_OFF);
    setup_timer_0(RTCC_INTERNAL);
    setup_timer_1(T1_DISABLED);
    setup_timer_2(T2_DISABLED,0,1);
    setup_timer_3(T3_DISABLED|T3_DIV_BY_1);

    /* set direction registers */
    set_tris_a(0x00);
    set_tris_c(0x00);

    #endif /* (defined(__PCB__) || defined(__PCH__) || defined(__PCM__)) */
}


/*******************************************************************************
 * FUNCTION:   main
 * PURPOSE:    Main Program.
 ******************************************************************************/
void main()
{
    sint16 temperature_raw;     /* temperature raw value (resolution 1/256°C) */
    float temperature_float;
    char temperature[8];        /* temperature as string */
    uint8 sensor_count;         /* sensor counter */


    /* initialize PIC */
    initialize();

    printf("\n\r*** DS1820 Demo! ***\n\r");

    /* main loop */
    while (1)
    {
        /* set LED on measurement start */
        LED1_On();
        
        sensor_count = 0;

        if ( DS1820_FindFirstDevice() )
        {
            do
            {
                /* get temperature raw value (resolution 1/256°C) */
                temperature_raw = DS1820_GetTempRaw();

                /* convert raw temperature to string for output */
                DS1820_GetTempString(temperature_raw, temperature);
                
                /* get temperature value as float */
                temperature_float = DS1820_GetTempFloat();
               

                /* print result to RS232 interface */
                #if defined(__18CXX)
                printf("Sensor %d: %s°C , temperature_raw = %d)\n\r",
                       sensor_count,
                       temperature,
                       temperature_raw);
                #endif /* #if defined(__18CXX) */

                /* CCS compiler specific */    
                #if (defined(__PCB__) || defined(__PCH__) || defined(__PCM__))
                printf("Sensor %d: %s°C (temperature_float = %f), temperature_raw = %ld)\n\r",
                       sensor_count,
                       temperature,
                       temperature_float,
                       temperature_raw);
                #endif /* (defined(__PCB__) || defined(__PCH__) || defined(__PCM__)) */
                

                sensor_count ++;
            }
            while ( DS1820_FindNextDevice() );

            sensor_count = 0;
        }

        /* measurement end, clear LED */
        LED1_Off();
      
        /* measure every 2 seconds */
        delay_ms(2000);
    }
}

