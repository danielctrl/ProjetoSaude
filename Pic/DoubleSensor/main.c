#include <main.h>

#define DS1820_DATAPIN PIN_B0

#include "types.h"
#include "ds1820.h"

void main()
{
    uint8 sensor_count;         /* sensor counter */

   setup_comparator(NC_NC_NC_NC);// This device COMP currently not supported by the PICWizard

   sensor_count = 0;

   while(TRUE)
   {

      if (DS1820_FindFirstDevice()) {
		  do
		  {
			printf("%i -- %05.1g\n\r", sensor_count, DS1820_GetTempFloat());
			sensor_count ++;
		  }
         while ( DS1820_FindNextDevice() );

		 sensor_count = 0;

      } else {
         printf("xx\n\r");
      }


      delay_ms(1000);

   }

}
