/*
 * main.c
 */

#include "DSP28x_Project.h"     // DSP28x Headerfile



#include "f2802x_common/include/clk.h"
#include "f2802x_common/include/flash.h"
#include "f2802x_common/include/pie.h"
#include "f2802x_common/include/gpio.h"
#include "f2802x_common/include/pll.h"
#include "f2802x_common/include/timer.h"
#include "f2802x_common/include/sci.h"
#include "f2802x_common/include/wdog.h"




void scia_echoback_init(void);
void scia_fifo_init(void);
void scia_xmit(int a);
void scia_msg(char *msg);

interrupt void inteWater(void);
interrupt void inteMotor(void);

unsigned long timer0IntCount;
unsigned long timer1IntCount;


CLK_Handle myClk;
FLASH_Handle myFlash;
GPIO_Handle myGpio;
PIE_Handle myPie;
TIMER_Handle myTimer0,myTimer1;

#ifdef _FLASH
    memcpy(&RamfuncsRunStart, &RamfuncsLoadStart, (size_t)&RamfuncsLoadSize);
#endif


void main()
{
 WDOG_Handle myWDog;
 myWDog = WDOG_init((void *)WDOG_BASE_ADDR, sizeof(WDOG_Obj));
 WDOG_disable(myWDog);

 CLK_Handle myClk;
 PLL_Handle myPll;
 myClk = CLK_init((void *)CLK_BASE_ADDR, sizeof(CLK_Obj));
 myPll = PLL_init((void *)PLL_BASE_ADDR, sizeof(PLL_Obj));
 myPie = PIE_init((void *)PIE_BASE_ADDR, sizeof(PIE_Obj));
 myTimer0 = TIMER_init((void *)TIMER0_BASE_ADDR, sizeof(TIMER_Obj));
 myTimer1 = TIMER_init((void *)TIMER0_BASE_ADDR, sizeof(TIMER_Obj));

 timer0IntCount = 0;
 timer0IntCount = 1;


  CLK_setOscSrc(myClk, CLK_OscSrc_Internal);

  PLL_setup(myPll, PLL_Multiplier_12, PLL_DivideSelect_ClkIn_by_2);

  GPIO_Handle myGpio;
  myGpio = GPIO_init((void *)GPIO_BASE_ADDR, sizeof(GPIO_Obj));

  GPIO_setMode(myGpio, GPIO_Number_0, GPIO_0_Mode_GeneralPurpose);
  GPIO_setDirection(myGpio, GPIO_Number_0, GPIO_Direction_Output);
  GPIO_setMode(myGpio, GPIO_Number_1, GPIO_1_Mode_GeneralPurpose);
  GPIO_setDirection(myGpio, GPIO_Number_1, GPIO_Direction_Output);
  GPIO_setMode(myGpio, GPIO_Number_2, GPIO_2_Mode_GeneralPurpose);
  GPIO_setDirection(myGpio, GPIO_Number_2, GPIO_Direction_Output);
  GPIO_setMode(myGpio, GPIO_Number_3, GPIO_3_Mode_GeneralPurpose);
  GPIO_setDirection(myGpio, GPIO_Number_3, GPIO_Direction_Output);

  GPIO_setHigh(myGpio, GPIO_Number_0);
  GPIO_setHigh(myGpio, GPIO_Number_1);
  GPIO_setHigh(myGpio, GPIO_Number_2);
  GPIO_setHigh(myGpio, GPIO_Number_3);

  PIE_registerPieIntHandler(myPie, PIE_GroupNumber_1, PIE_SubGroupNumber_7, (intVec_t)&cpu_timer0_isr);

  EDIS;

  TIMER_stop(myTimer0);
  TIMER_stop(myTimer1);


#if (CPU_FRQ_50MHZ)

    TIMER_setPeriod(myTimer0, 50 * 1000000);

#endif


TIMER_setPreScaler(myTimer0, 0);
TIMER_reload(myTimer0);
TIMER_setEmulationMode(myTimer0, TIMER_EmulationMode_StopAfterNextDecrement);
TIMER_enableInt(myTimer0);

TIMER_start(myTimer0);

TIMER_setPreScaler(myTimer1, 1);
TIMER_reload(myTimer1);
TIMER_setEmulationMode(myTimer1, TIMER_EmulationMode_StopAfterNextDecrement);
TIMER_enableInt(myTimer1);

TIMER_start(myTimer1);

CPU_enableInt(myCpu, CPU_IntNumber_1);
CPU_enableInt(myCpu, CPU_IntNumber_2);


PIE_enableTimer0Int(myPie);
  while(1)
  {


  }

}


interrupt void inteWater(void)
{

	//actvate the water valve
    timer0IntCount++;
    if(timer0IntCount == 10){
    	 GPIO_setLow(myGpio, GPIO_Number_1);
    	    DELAY_US(1000000);
    	    GPIO_setHigh(myGpio, GPIO_Number_1);
    }
    PIE_clearInt(myPie, PIE_GroupNumber_1);
}


interrupt void inteMotor(void)
{

	//activate the motor
    timer0IntCount++;
    if(timer0IntCount == 10){
    	 GPIO_setLow(myGpio, GPIO_Number_2);
    	    DELAY_US(1000000);
    	    GPIO_setHigh(myGpio, GPIO_Number_2);
    }
    PIE_clearInt(myPie, PIE_GroupNumber_2);
}


// Test 1,SCIA  DLB, 8-bit word, baud rate 0x000F, default, 1 STOP bit, no parity
void scia_echoback_init()
{

    CLK_enableSciaClock(myClk);

    // 1 stop bit,  No loopback
    // No parity,8 char bits,
    // async mode, idle-line protocol
    SCI_disableParity(mySci);
    SCI_setNumStopBits(mySci, SCI_NumStopBits_One);
    SCI_setCharLength(mySci, SCI_CharLength_8_Bits);

    SCI_enableTx(mySci);
    SCI_enableRx(mySci);
    SCI_enableTxInt(mySci);
    SCI_enableRxInt(mySci);

    // SCI BRR = LSPCLK/(SCI BAUDx8) - 1
#if (CPU_FRQ_50MHZ)
    SCI_setBaudRate(mySci, SCI_BaudRate_9_6_kBaud);
#elif (CPU_FRQ_40MHZ)
    SCI_setBaudRate(mySci, (SCI_BaudRate_e)129);
#endif

    SCI_enable(mySci);

    return;
}

// Transmit a character from the SCI
void scia_xmit(int a)
{
//    while (SciaRegs.SCIFFTX.bit.TXFFST != 0) {}
    while(SCI_getTxFifoStatus(mySci) != SCI_FifoStatus_Empty){
    }
//    SciaRegs.SCITXBUF=a;
    SCI_write(mySci, a);

}

void scia_msg(char * msg)
{
    int i;
    i = 0;
    while(msg[i] != '\0')
    {
        scia_xmit(msg[i]);
        i++;
    }
}

// Initalize the SCI FIFO
void scia_fifo_init()
{

    SCI_enableFifoEnh(mySci);
    SCI_resetTxFifo(mySci);
    SCI_clearTxFifoInt(mySci);
    SCI_resetChannels(mySci);
    SCI_setTxFifoIntLevel(mySci, SCI_FifoLevel_Empty);

    SCI_resetRxFifo(mySci);
    SCI_clearRxFifoInt(mySci);
    SCI_setRxFifoIntLevel(mySci, SCI_FifoLevel_4_Words);

    return;
}

