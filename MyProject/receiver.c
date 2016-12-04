#include <fcntl.h>
#include <sys/stat.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <wiringPi.h>
#include <signal.h>

#define MSG_SIZE 80

#define OUT1 0
#define OUT2 2
#define PWM1 1

#define OUT3 24
#define OUT4 25
#define PWM2 23

#define ULTRAPIN 29

int outpin1 = 0;
int outpin2 = 0;
int outpin3 = 0;
int outpin4 = 0;

int pwmpin1 = 0;
int pwmpin2 = 0;
int pwmpinNum = 125;

void ultraIntterrupt(void)
{
    if(!digitalRead(ULTRAPIN))
    {
        digitalWrite(OUT1, outpin1);
        digitalWrite(OUT2, outpin2);
        digitalWrite(OUT3, outpin3);
        digitalWrite(OUT4, outpin4);
    }
    else
    {
        digitalWrite(OUT1, 0);
        digitalWrite(OUT2, 0);
        digitalWrite(OUT3, 0);
        digitalWrite(OUT4, 0);    
    }
}


int main(void)
{
    char msg[MSG_SIZE];
    int filedes;
    int nread, cnt;
    
//    int outpin1 = 0;
//    int outpin2 = 0;
//    int outpin3 = 0;
//    int outpin4 = 0;
    
//    int pwmpin1 = 0;
//    int pwmpin2 = 0;
//    int pwmpinNum = 125;

    if(wiringPiSetup() == -1)
        return 1;

    pinMode(OUT1, OUTPUT);
    pinMode(OUT2, OUTPUT);
    pinMode(PWM1, OUTPUT);
    
    pinMode(OUT3, OUTPUT);
    pinMode(OUT4, OUTPUT);
    pinMode(PWM2, OUTPUT);

    softPwmCreate(PWM1, 0, 255);
    softPwmCreate(PWM2, 0, 255);
    
    pinMode(ULTRAPIN, INPUT);


    digitalWrite(OUT1, 0);
    digitalWrite(OUT2, 0);
    digitalWrite(OUT3, 0);
    digitalWrite(OUT4, 0);
    softPwmWrite(PWM1, 0);
    softPwmWrite(PWM2, 0);

    unlink("./fifo");

    if(mkfifo("./fifo",0666) == -1){
        printf("fail to call fifo()\n");
        exit(1);
    }

    if((filedes = open("./fifo", O_RDWR)) < 0){
        printf("fail to call fifo()\n");
        exit(1);
    }
    
    if(wiringPiISR(ULTRAPIN, INT_EDGE_BOTH, &ultraIntterrupt) < 0)
    {
        printf("HI");
    }

    for(; ; )
	{
        if((nread = read(filedes, msg, MSG_SIZE)) < 0 )
        {
            printf("fail to call read()\n");
            exit(1);
        }
        else
	    {
		    digitalWrite(OUT1, outpin1);
		    digitalWrite(OUT2, outpin2);
		    softPwmWrite(PWM1, pwmpin1);
		
		    digitalWrite(OUT3, outpin3);
		    digitalWrite(OUT4, outpin4);
		    softPwmWrite(PWM2, 255);
	   }
 	    if('f' == msg[0])
	    {
		    outpin1 = 1;
	    	outpin2 = 0;
		    pwmpin1 = pwmpinNum;
	    }
	    else if('b' == msg[0])
	    {
		    outpin1 = 0;
		    outpin2 = 1;
		    pwmpin1 = pwmpinNum;
	    }
	    else if('k' == msg[0])
	    {
		    outpin1 = 0;
		    outpin2 = 0;
		    pwmpin1 = pwmpinNum;
	    }
	    else if('l' == msg[0])
	    {
		    outpin3 = 0;
		    outpin4 = 1;
		    pwmpin2 = 255;
	    }
	    else if('r' == msg[0])
	    {
		    outpin3 = 1;
		    outpin4 = 0;
		    pwmpin2 = 255;
	    }
	    else if('s' == msg[0])
	    {
		    outpin3 = 0;
		    outpin4 = 0;
		    pwmpin2 = 255;
	    }
        else if('H' == msg[0])
        {
            pwmpinNum = 255;
            printf("HIGH");
        }
        else if('L' == msg[0])
        {
            pwmpinNum = 125;
            printf("LOW");   
        }
        
             //printf("outpin1 : %d, outpin2 : %d\n", outpin1, outpin2);
        digitalWrite(OUT1, outpin1);
        digitalWrite(OUT2, outpin2);
        digitalWrite(OUT3, outpin3);
        digitalWrite(OUT4, outpin4);
        softPwmWrite(PWM1, pwmpin1);
        softPwmWrite(PWM2, pwmpin2);
             
        printf("recv: %c\n", msg[0]);
    }

    unlink("./fifo");

    return 0;
}

/*

int function(){
    
    int pid, ret=1;
    int size=0;

    if((pid=fork()) <0){
        return 1;
    }else if(pid > 0){
        _exit(0);
    }

    if(wiringPiSetup() == -1)
        return 1;

    if(setsid() == -1)
        _exit(0);

    if(chdir("/") == -1)
        _exit(0);
  
    umask(0);
    signal(SIGHUP, SIG_IGN);

    close(0);
    close(1);
    close(2);
    stdin = freopen("/dev/null", "r", stdin);
    stdout = freopen("/dev/null", "w", stdout);
    stderr = freopen("/dev/null", "w", stderr);
    
    doTask();
}
*/
