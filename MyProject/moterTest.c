#include <stdio.h>
#include <wiringPi.h>
#include <softPwm.h>

#define OUT1 0
#define OUT2 2
#define PWM1 1

#define OUT3 24
#define OUT4 25
#define PWM2 23

int main(void)
{
	int outpin1 = 0;
	int outpin2 = 1;

	if(wiringPiSetup () == -1)
		return 1;

	pinMode(OUT1, OUTPUT);
	pinMode(OUT2, OUTPUT);
	pinMode(PWM1, OUTPUT);
	
	pinMode(OUT3, OUTPUT);
	pinMode(OUT4, OUTPUT);
	pinMode(PWM2, OUTPUT);

	softPwmCreate(PWM1, 0, 255);
	softPwmCreate(PWM2, 0, 255);

//	digitalWrite(OUT1, outpin1);
//	digitalWrite(OUT2, outpin2);
//	softPwmWrite(PWM1, 255);
	

	while(1)
	{
		
		digitalWrite(OUT1, 1);
		digitalWrite(OUT2, 0);
		softPwmWrite(PWM1, 225);

//		digitalWrite(OUT3, 1);
//		digitalWrite(OUT4, 0);
//		softPwmWrite(PWM2, 255);
	}
	
	return 0;
}
