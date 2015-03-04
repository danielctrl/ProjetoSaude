#ifndef _DS1820DEMO_H

/*---------------------------------------------------------------------------*/
/* Configuration for Microchip C18 Compiler                                  */
/*---------------------------------------------------------------------------*/
#if defined(__18CXX)

/* compile specific include files */
#include <p18cxxx.h>
#include <delays.h>
#include <stdio.h>
#include <usart.h>
#include <string.h>

#include "types.h"


/* configuration bits */
#pragma config OSC = HSPLL
#pragma config WDT = OFF
#pragma config LVP = ON


#define set_tris_a(value)   TRISA = (value)
#define set_tris_c(value)   TRISC = (value)

#ifndef FALSE  
#define FALSE 0
#endif

#ifndef TRUE
#define TRUE 1
#endif


/*******************************************************************************
 * FUNCTION:   void delay_us(uint usec)
 * PURPOSE:    delay for the given number of microseconds
 ******************************************************************************/
/* asm_timed_delay is found in an assembly file */
extern void delay_asm_c18_32mhz(sint16 usec);

#define delay_us    delay_asm_c18_32mhz



/*******************************************************************************
 * FUNCTION:   void delay_ms(uint16 msec)
 * PURPOSE:    delay for the given number of miliseconds
 ******************************************************************************/
void delay_ms(uint16 msec)
{
    while(msec!=0)
    {
        msec--;
        Delay100TCYx(80);
    }
}

#define LED1_On()   LATCbits.LATC0 = 1 // PIN_C0
#define LED1_Off()  LATCbits.LATC0 = 0 // PIN_C0
#define LED2_On()   LATCbits.LATC1 = 1 // PIN_C1
#define LED2_Off()  LATCbits.LATC1 = 0 // PIN_C1

#endif /* #if defined(__18CXX) */




/*---------------------------------------------------------------------------*/
/* Configuration for CCS C Compiler                                          */
/*---------------------------------------------------------------------------*/
#if (defined(__PCB__) || defined(__PCH__) || defined(__PCM__))

#include <18F458.h>
#device adc=8
#use delay(clock=32000000)
#fuses NOWDT,WDT128,H4, NOPROTECT, NOOSCSEN, BROWNOUT, BORV20, PUT, STVREN, NODEBUG, LVP, NOWRT, NOWRTD, NOWRTB, NOWRTC, NOCPD, NOCPB, NOEBTR, NOEBTRB
#use rs232(baud=19200,parity=N,xmit=PIN_C6,rcv=PIN_C7,bits=8)

#define LED1_On()       output_high(PIN_C0);
#define LED1_Off()      output_low(PIN_C0);
#define LED2_On()       output_high(PIN_C1);
#define LED2_Off()      output_low(PIN_C1);

#endif /* (defined(__PCB__) || defined(__PCH__) || defined(__PCM__)) */

#endif /* #ifndef _DS1820DEMO_H */


