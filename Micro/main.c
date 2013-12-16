#include "DSP28x_Project.h"     // Device Headerfile and Examples Include File

#include "f2802x_common/include/adc.h"
#include "f2802x_common/include/clk.h"
#include "f2802x_common/include/flash.h"
#include "f2802x_common/include/gpio.h"
#include "f2802x_common/include/pie.h"
#include "f2802x_common/include/pll.h"
#include "f2802x_common/include/sci.h"
#include "f2802x_common/include/timer.h"
#include "f2802x_common/include/wdog.h"


interrupt void cpu_timer0_isr(void);
void scia_echoback_init(void);
void scia_fifo_init(void);
void scia_xmit(int a);
void scia_msg(char *msg);
void hexDubug( uint16_t temp2);
void printWeight(uint16_t weight );
uint16_t readADC(char * msg, uint16_t step,int opcion);


#define CONV_WAIT 1L



ADC_Handle myAdc;
CLK_Handle myClk;
FLASH_Handle myFlash;
GPIO_Handle myGpio;
PIE_Handle myPie;
SCI_Handle mySci;
TIMER_Handle myTimer;

void main(void)
{
    uint16_t ReceivedChar;


    char *msg;
    char queue[]= {'0','0','0','0'};




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
    mySci = SCI_init((void *)SCIA_BASE_ADDR, sizeof(SCI_Obj));
    myTimer = TIMER_init((void *)TIMER0_BASE_ADDR, sizeof(TIMER_Obj));
    myWDog = WDOG_init((void *)WDOG_BASE_ADDR, sizeof(WDOG_Obj));

    // Perform basic system initialization
    WDOG_disable(myWDog);
    CLK_enableAdcClock(myClk);
    (*Device_cal)();

    //Select the internal oscillator 1 as the clock source
    CLK_setOscSrc(myClk, CLK_OscSrc_Internal);

    // Setup the PLL for x10 /2 which will yield 50Mhz = 10Mhz * 10 / 2
    PLL_setup(myPll, PLL_Multiplier_12, PLL_DivideSelect_ClkIn_by_2);

    // Disable the PIE and all interrupts
    PIE_disable(myPie);
    PIE_disableAllInts(myPie);
    CPU_disableGlobalInts(myCpu);
    CPU_clearIntFlags(myCpu);

    // If running from flash copy RAM only functions to RAM
#ifdef _FLASH
    memcpy(&RamfuncsRunStart, &RamfuncsLoadStart, (size_t)&RamfuncsLoadSize);
#endif


    // Initalize GPIO
    GPIO_setMode(myGpio, GPIO_Number_0, GPIO_0_Mode_GeneralPurpose);
    GPIO_setDirection(myGpio, GPIO_Number_0, GPIO_Direction_Output);
    GPIO_setMode(myGpio, GPIO_Number_1, GPIO_1_Mode_GeneralPurpose);
    GPIO_setDirection(myGpio, GPIO_Number_1, GPIO_Direction_Output);
    GPIO_setMode(myGpio, GPIO_Number_2, GPIO_2_Mode_GeneralPurpose);
    GPIO_setDirection(myGpio, GPIO_Number_2, GPIO_Direction_Output);

    GPIO_setPullUp(myGpio, GPIO_Number_28, GPIO_PullUp_Enable);
    GPIO_setPullUp(myGpio, GPIO_Number_29, GPIO_PullUp_Disable);
    GPIO_setPullUp(myGpio, GPIO_Number_19, GPIO_PullUp_Enable);
    GPIO_setPullUp(myGpio, GPIO_Number_12, GPIO_PullUp_Disable);
    GPIO_setQualification(myGpio, GPIO_Number_28, GPIO_Qual_ASync);


    GPIO_setMode(myGpio, GPIO_Number_28, GPIO_28_Mode_SCIRXDA);
    GPIO_setMode(myGpio, GPIO_Number_29, GPIO_29_Mode_SCITXDA);

    GPIO_setMode(myGpio, GPIO_Number_18, GPIO_18_Mode_XCLKOUT);

    CLK_setClkOutPreScaler(myClk, CLK_ClkOutPreScaler_SysClkOut_by_1);



    ADC_enableBandGap(myAdc);
    ADC_enableRefBuffers(myAdc);
    ADC_powerUp(myAdc);
    ADC_enable(myAdc);
    ADC_setVoltRefSrc(myAdc, ADC_VoltageRefSrc_Int);

  //  ADC_enableTempSensor(myAdc);
    //ADC pins water bucket
    ADC_setSocChanNumber (myAdc, ADC_SocNumber_0, ADC_SocChanNumber_A4);
    ADC_setSocChanNumber (myAdc, ADC_SocNumber_1, ADC_SocChanNumber_B4);
    ADC_setSocChanNumber (myAdc, ADC_SocNumber_2, ADC_SocChanNumber_B7);
    ADC_setSocChanNumber (myAdc, ADC_SocNumber_3, ADC_SocChanNumber_B3);

    //ADC pins food buckect
    ADC_setSocChanNumber (myAdc, ADC_SocNumber_4, ADC_SocChanNumber_A1);
    ADC_setSocChanNumber (myAdc, ADC_SocNumber_5, ADC_SocChanNumber_A0);
    ADC_setSocChanNumber (myAdc, ADC_SocNumber_6, ADC_SocChanNumber_B1);

    ADC_setSocChanNumber (myAdc, ADC_SocNumber_7, ADC_SocChanNumber_A3);

    //pin for flow sensor
    ADC_setSocChanNumber (myAdc, ADC_SocNumber_8, ADC_SocChanNumber_A7);




    ADC_setSocSampleWindow(myAdc, ADC_SocNumber_0, ADC_SocSampleWindow_64_cycles);
    ADC_setSocSampleWindow(myAdc, ADC_SocNumber_1, ADC_SocSampleWindow_64_cycles);
    ADC_setSocSampleWindow(myAdc, ADC_SocNumber_2, ADC_SocSampleWindow_64_cycles);
    ADC_setSocSampleWindow(myAdc, ADC_SocNumber_3, ADC_SocSampleWindow_64_cycles);
    ADC_setSocSampleWindow(myAdc, ADC_SocNumber_4, ADC_SocSampleWindow_64_cycles);
    ADC_setSocSampleWindow(myAdc, ADC_SocNumber_5, ADC_SocSampleWindow_64_cycles);
    ADC_setSocSampleWindow(myAdc, ADC_SocNumber_6, ADC_SocSampleWindow_64_cycles);
    ADC_setSocSampleWindow(myAdc, ADC_SocNumber_7, ADC_SocSampleWindow_64_cycles);
    ADC_setSocSampleWindow(myAdc, ADC_SocNumber_8, ADC_SocSampleWindow_64_cycles);




    ADC_setIntSrc(myAdc, ADC_IntNumber_1, ADC_IntSrc_EOC1);
    ADC_setIntSrc(myAdc, ADC_IntNumber_2, ADC_IntSrc_EOC2);
    ADC_enableInt(myAdc, ADC_IntNumber_1);
    ADC_enableInt(myAdc, ADC_IntNumber_2);


    FLASH_setup(myFlash);

    // Setup a debug vector table and enable the PIE
    PIE_setDebugIntVectorTable(myPie);
    PIE_enable(myPie);

    PIE_registerPieIntHandler(myPie, PIE_GroupNumber_1, PIE_SubGroupNumber_7, (intVec_t)&cpu_timer0_isr);


    TIMER_stop(myTimer);
    TIMER_setPeriod(myTimer, 50 * 5000000);
    TIMER_setPreScaler(myTimer, 0);
    TIMER_reload(myTimer);
    TIMER_setEmulationMode(myTimer, TIMER_EmulationMode_StopAfterNextDecrement);
    TIMER_enableInt(myTimer);

    TIMER_start(myTimer);

    CPU_enableInt(myCpu, CPU_IntNumber_1);

        // Enable TINT0 in the PIE: Group 1 interrupt 7
    PIE_enableTimer0Int(myPie);

        // Enable global Interrupts and higher priority real-time debug events
    CPU_enableGlobalInts(myCpu);
    CPU_enableDebugInt(myCpu);


    scia_echoback_init();  // Initalize SCI for echoback
    scia_fifo_init();      // Initialize the SCI FIFO


    //Calibration variables for adc measurements

      uint16_t step=0x0029,peso=0,water=0;
    //Command Array
      uint16_t Command[] = {0x0061, 0x0062,0x0063, 0x0064,0x0065,0x0066,0x0067,0x0068};



    msg = "\r\n Operacion Enigma 7.20 \n\0";
    scia_msg(msg);

    msg = "\r\n Que comienze la comunicacion \n\0";
    scia_msg(msg);

    for(;;)
    {
        // Wait for inc character
        while(SCI_getRxFifoStatus(mySci) < SCI_FifoStatus_1_Word){

        }


        // Get character
        ReceivedChar =  SCI_getData(mySci);
        //if a is sent turn on both the valve and the motor
        if(ReceivedChar == Command[0]){
        	GPIO_toggle(myGpio, GPIO_Number_0);
        //	 DELAY_US(18000000);
        	//GPIO_toggle(myGpio, GPIO_Number_0);
        	 peso=0;
        	 while(peso<=0x0450){
        		// GPIO_toggle(myGpio, GPIO_Number_0);
        		 DELAY_US(1000000);
        		 peso=readADC(msg,step, 2);
        		 hexDubug(peso);
        	 }

        	 GPIO_toggle(myGpio, GPIO_Number_0);
        /// GPIO_setLow(myGpio, GPIO_Number_0);

        	/** GPIO_setHigh(myGpio, GPIO_Number_1);
        	// GPIO_setHigh(myGpio, GPIO_Number_0);
        	 msg = "mmm";
        	 scia_msg(msg);

        	 peso=0;
        	 while(peso<=0x400){
        	 DELAY_US(3000000);
        	 peso=readADC(msg,step, 1);
        	 msg = "\r peso final \n\0";
        	 scia_msg(msg);
        	 hexDubug(peso);
        	 }
        	 GPIO_setLow(myGpio, GPIO_Number_1);
        	 GPIO_setLow(myGpio, GPIO_Number_0);
			*/
        } else if(ReceivedChar == Command[7]){

        	GPIO_toggle(myGpio, GPIO_Number_1);
                //	 DELAY_US(18000000);
                	//GPIO_toggle(myGpio, GPIO_Number_0);


                		// GPIO_toggle(myGpio, GPIO_Number_0);
                		 DELAY_US(32000000);



                	 GPIO_toggle(myGpio, GPIO_Number_1);

        }
        // Read the current temperature of the cattle and send latest weight and if the silo if full or not
        else if(ReceivedChar == Command[1]){
        				int o = 0;
        	        	GPIO_setLow(myGpio, GPIO_Number_28);
        	        	GPIO_setLow(myGpio, GPIO_Number_29);
        	        	GPIO_setMode(myGpio, GPIO_Number_28, GPIO_28_Mode_GeneralPurpose);
        	        	GPIO_setMode(myGpio, GPIO_Number_29, GPIO_29_Mode_GeneralPurpose);
        	        	GPIO_setMode(myGpio, GPIO_Number_19, GPIO_19_Mode_SCIRXDA);
        	        	GPIO_setMode(myGpio, GPIO_Number_12, GPIO_12_Mode_SCITXDA);
        	        	while(ReceivedChar!='t'){
        	        		if(o>4){
        	        			o=0;
        	        		}
        	        	while(SCI_getRxFifoStatus(mySci) < SCI_FifoStatus_1_Word){


        	        	}
        	        	 ReceivedChar =  SCI_getData(mySci);
        	        	 queue[o]=ReceivedChar;
        	        	scia_xmit(ReceivedChar);
        	        	o++;
        	        	}

        	        	GPIO_setLow(myGpio, GPIO_Number_19);
        	        	GPIO_setLow(myGpio, GPIO_Number_12);
        	        	GPIO_setMode(myGpio, GPIO_Number_12, GPIO_12_Mode_GeneralPurpose);
        	        	GPIO_setMode(myGpio, GPIO_Number_19, GPIO_19_Mode_GeneralPurpose);
        	        	GPIO_setMode(myGpio, GPIO_Number_28, GPIO_28_Mode_SCIRXDA);
        	        	GPIO_setMode(myGpio, GPIO_Number_29, GPIO_29_Mode_SCITXDA);
        	        	for(o=0;o<4;o++){
        	        	scia_xmit(queue[o]);
        	        	}



        	        	peso=readADC(msg,step, 1);
        	        	scia_xmit('p');
        	        	hexDubug(peso);


        	        	scia_xmit('s');

        	        	if(readADC(msg,step, 3)>=0x0333){
        	        		scia_xmit('1');
        	        	}
        	        	else{
        	        		scia_xmit('0');
        	        	}


                }




        //read the weight of the food bucket
        else if(ReceivedChar == Command[2]){

        	peso=readADC(msg,step, 3);
        	msg = "\r peso final \n\0";
        	scia_msg(msg);
        	hexDubug(peso);
        	//peso=readADC(msg,step, 4);
        	//msg = "\r peso final \n\0";
        	//scia_msg(msg);
        	//hexDubug(peso);
        }

        //comando para leer el ADC y enviar el resultado verificando si el tanque esta lleno o no
        else if(ReceivedChar ==Command[3]){

         readADC(msg,step, 3);

         hexDubug(step);
        }

        //command to recalibrate the weight sensors
        else if(ReceivedChar == Command[4]){


       	 uint16_t ceroValB1=0;
       	 ceroValB1= readADC(msg,step, 1);
       	 hexDubug(ceroValB1);
       	 step=(0xFFF-ceroValB1)/0x0064;



      	 hexDubug(step);


       	//First callibration function
         msg = "\r reset de valdes \n\0";
         scia_msg(msg);

       }else{
    	   msg = "VCEC";
    	   scia_msg(msg);
    	   scia_fifo_init();

       }


    }

}


uint16_t readADC(char * msg, uint16_t step,int opcion){


	ADC_forceConversion(myAdc, ADC_SocNumber_0);
	ADC_forceConversion(myAdc, ADC_SocNumber_1);
	ADC_forceConversion(myAdc, ADC_SocNumber_2);
	ADC_forceConversion(myAdc, ADC_SocNumber_3);

	ADC_forceConversion(myAdc, ADC_SocNumber_4);
	ADC_forceConversion(myAdc, ADC_SocNumber_5);
	ADC_forceConversion(myAdc, ADC_SocNumber_6);

	ADC_forceConversion(myAdc, ADC_SocNumber_7);

	ADC_forceConversion(myAdc, ADC_SocNumber_8);




	         //Wait for end of conversion.
	         while(ADC_getIntStatus(myAdc, ADC_IntNumber_1) == 0) {
	               	        }

	               	        // Clear ADCINT1
	         ADC_clearIntFlag(myAdc, ADC_IntNumber_1);

	         uint16_t temp;


	         if(opcion==1){
	         temp = ADC_readResult(myAdc, ADC_ResultNumber_4)+ADC_readResult(myAdc, ADC_ResultNumber_5)+ADC_readResult(myAdc, ADC_ResultNumber_6);
	         temp = temp/(3);
	         double pesoReal=0;
	         pesoReal=(3.32336*(temp*(3.3/4096))-1.84746)*1000;
	         temp=(uint16_t)pesoReal;


	         }
	         else if(opcion==2){
	         temp = ADC_readResult(myAdc, ADC_ResultNumber_0)+ADC_readResult(myAdc, ADC_ResultNumber_1)+ADC_readResult(myAdc, ADC_ResultNumber_3)+ADC_readResult(myAdc, ADC_ResultNumber_2);
	         temp = temp/(4);

	         }
	         else if(opcion==3){
	         temp =ADC_readResult(myAdc, ADC_ResultNumber_7);
	         }
	         else{
	         temp =ADC_readResult(myAdc, ADC_ResultNumber_8);
	         }




	         return temp;

}


void printWeight(uint16_t weight ){
	uint8_t lookup[] = {0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39};

	if(weight<100){
	  scia_xmit(lookup[weight/10]);
	  scia_xmit(lookup[weight%10]);
	}
	else{
		scia_xmit(lookup[1]);
		scia_xmit(lookup[0]);
		scia_xmit(lookup[0]);

	}
}


void hexDubug( uint16_t temp2){

	int i,j;
	uint8_t lookup[] = {0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0x41,0x42,0x43,0x44,0x45,0x46};
	uint8_t temp3;
    char hex[4];
	 for( i = 0; i<4;i++){
		 temp3= (0x0F & temp2);
		 for( j = 0; j<16;j++){
			 if(temp3==j){
				 hex[3-i]=lookup[j];
			 //scia_xmit(lookup[j]);
			 }
		 }
		 temp2 =temp2>>4;
	 }

	 for(i=0;i<4;i++){
		 scia_xmit(hex[i]);
	 }

	return;
}



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
#if (CPU_FRQ_60MHZ)
    SCI_setBaudRate(mySci, SCI_BaudRate_9_6_kBaud);
#elif (CPU_FRQ_50MHZ)
    SCI_setBaudRate(mySci, (SCI_BaudRate_e)129);
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
    SCI_putData(mySci, a);

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


interrupt void cpu_timer0_isr(void)
{

    // Toggle GPIOs

   // GPIO_toggle(myGpio, GPIO_Number_1);


    // Acknowledge this interrupt to receive more interrupts from group 1
    PIE_clearInt(myPie, PIE_GroupNumber_1);
}
