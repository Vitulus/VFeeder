#include "DSP28x_Project.h"

#include "f2802x_common/include/clk.h"
#include "f2802x_common/include/flash.h"
#include "f2802x_common/include/gpio.h"
#include "f2802x_common/include/pie.h"
#include "f2802x_common/include/pll.h"
#include "f2802x_common/include/timer.h"
#include "f2802x_common/include/wdog.h"


interrupt void xint1_isr(void);
interrupt void cpu_timer0_isr(void);

uint16_t interruptCount = 0;

ADC_Handle myAdc;
CLK_Handle myClk;
FLASH_Handle myFlash;
GPIO_Handle myGpio;
PIE_Handle myPie;
TIMER_Handle myTimer;

void main(void)
  {
CPU_Handle myCpu;
PLL_Handle myPll;
WDOG_Handle myWDog;

// Initialize all the handles needed for this application
myAdc = ADC_init((void *)ADC_BASE_ADDR, sizeof(ADC_Obj));
myClk = CLK_init((void *)CLK_BASE_ADDR, sizeof(CLK_Obj));
myCpu = CPU_init((void *)NULL, sizeof(CPU_Obj));
myFlash = FLASH_init((void *)FLASH_BASE_ADDR, sizeof(FLASH_Obj));
myGpio = GPIO_init((void *)GPIO_BASE_ADDR, sizeof(GPIO_Obj));
myPie = PIE_init((void *)PIE_BASE_ADDR, sizeof(PIE_Obj));
myPll = PLL_init((void *)PLL_BASE_ADDR, sizeof(PLL_Obj));
myTimer = TIMER_init((void *)TIMER0_BASE_ADDR, sizeof(TIMER_Obj));
myWDog = WDOG_init((void *)WDOG_BASE_ADDR, sizeof(WDOG_Obj));

// Perform basic system initialization
WDOG_disable(myWDog);
CLK_enableAdcClock(myClk);
(*Device_cal)();

//Select the internal oscillator 1 as the clock source
CLK_setOscSrc(myClk, CLK_OscSrc_Internal);

// Setup the PLL for x10 /2 which will yield 50Mhz = 10Mhz * 10 / 2
PLL_setup(myPll, PLL_Multiplier_10, PLL_DivideSelect_ClkIn_by_2);

// Disable the PIE and all interrupts
PIE_disable(myPie);
PIE_disableAllInts(myPie);
CPU_disableGlobalInts(myCpu);
CPU_clearIntFlags(myCpu);

// If running from flash copy RAM only functions to RAM
#ifdef _FLASH
memcpy(&RamfuncsRunStart, &RamfuncsLoadStart, (size_t)&RamfuncsLoadSize);
#endif

// Setup a debug vector table and enable the PIE
PIE_setDebugIntVectorTable(myPie);
PIE_enable(myPie);

// Register interrupt handlers in the PIE vector table
PIE_registerPieIntHandler(myPie, PIE_GroupNumber_1, PIE_SubGroupNumber_4, (intVec_t)&xint1_isr);
PIE_registerPieIntHandler(myPie, PIE_GroupNumber_1, PIE_SubGroupNumber_7, (intVec_t)&cpu_timer0_isr);
// Enable XINT1 in the PIE: Group 1 interrupt 4
// Enable INT1 which is connected to WAKEINT
PIE_enableInt(myPie, PIE_GroupNumber_1, PIE_InterruptSource_XINT_1);
CPU_enableInt(myCpu, CPU_IntNumber_1);



TIMER_stop(myTimer);
TIMER_setPeriod(myTimer, 50 * 500000);
TIMER_setPreScaler(myTimer, 0);
TIMER_reload(myTimer);
TIMER_setEmulationMode(myTimer, TIMER_EmulationMode_StopAfterNextDecrement);
TIMER_enableInt(myTimer);
TIMER_start(myTimer);



// Enable Global Interrupts
CPU_enableGlobalInts(myCpu);

// Configure GPIO 0-3 as outputs
GPIO_setMode(myGpio, GPIO_Number_0, GPIO_0_Mode_GeneralPurpose);
GPIO_setMode(myGpio, GPIO_Number_1, GPIO_0_Mode_GeneralPurpose);
GPIO_setMode(myGpio, GPIO_Number_2, GPIO_0_Mode_GeneralPurpose);
GPIO_setMode(myGpio, GPIO_Number_3, GPIO_0_Mode_GeneralPurpose);

GPIO_setDirection(myGpio, GPIO_Number_0, GPIO_Direction_Output);
GPIO_setDirection(myGpio, GPIO_Number_1, GPIO_Direction_Output);
GPIO_setDirection(myGpio, GPIO_Number_2, GPIO_Direction_Output);
GPIO_setDirection(myGpio, GPIO_Number_3, GPIO_Direction_Output);

// GPIO12 input
GPIO_setMode(myGpio, GPIO_Number_12, GPIO_12_Mode_GeneralPurpose);
GPIO_setDirection(myGpio, GPIO_Number_12, GPIO_Direction_Input);
GPIO_setPullUp(myGpio, GPIO_Number_12, GPIO_PullUp_Disable);

// GPIO12 is XINT1
GPIO_setExtInt(myGpio, GPIO_Number_12, CPU_ExtIntNumber_1);
CPU_enableInt(myCpu, CPU_IntNumber_1);
// Configure XINT1
PIE_setExtIntPolarity(myPie, CPU_ExtIntNumber_1, PIE_ExtIntPolarity_FallingEdge);

// Enable XINT1
PIE_enableExtInt(myPie, CPU_ExtIntNumber_1);
PIE_enableTimer0Int(myPie);


CPU_enableGlobalInts(myCpu);
CPU_enableDebugInt(myCpu);

for(;;) {

}
}

interrupt void xint1_isr(void)
{



	 GPIO_toggle(myGpio, GPIO_Number_1);
	 GPIO_toggle(myGpio, GPIO_Number_2);
	 GPIO_toggle(myGpio, GPIO_Number_3);


PIE_clearInt(myPie, PIE_GroupNumber_1);
}



interrupt void cpu_timer0_isr(void)
{
    interruptCount++;


    GPIO_toggle(myGpio, GPIO_Number_0);


    PIE_clearInt(myPie, PIE_GroupNumber_1);
}

