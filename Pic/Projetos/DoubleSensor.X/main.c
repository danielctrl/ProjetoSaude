#include <main.h>

#define DS1820_DATAPIN PIN_B0

#include "types.h"
#include "ds1820.h"

void main()
{
   setup_comparator(NC_NC_NC_NC);// This device COMP currently not supported by the PICWizard

   while(TRUE)
   {
      if (DS1820_FindFirstDevice()) {
         printf("%05.1g\n\r",DS1820_GetTempFloat());
      } else {
         printf("xxx.x\n\r");
      }      
      delay_ms(1000);
   }

}
