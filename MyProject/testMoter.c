#include <stdio.h>
#include <stdlib.h>
#include <wiringPi.h>

#define OUT1 0
#define OUT2 2
#define PWM1 1

int main()
{
	int outpin1 = 0;
	int outpin2 = 0;
	int pwmpin1 = 0;

	if(wiringPiSetup() == -1)
		return 1;

	pinMode(OUT1, OUTPUT);
	pinMode(OUT2, OUTPUT);
	pinMode(PWM1, OUTPUT);

	softPwmCreate(PWM1, 0, 255);

	for( ; ; ) 
	{
		digitalWrite(OUT1, outpin1);
		digitalWrite(OUT2, outpin2);
		softPWMWrite(PWM1, pwmpin1);
	}
	
}
