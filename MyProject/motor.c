#include <stdio.h>
#include <wiringPi.h>
#include <softPwm.h>
#include <time.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <string.h>

#include "control.h"


#define SUCCESS 0
#define FAIL -1

int led_obs_status = 0;

void setup(){

    pinMode(MOTOR_1, OUTPUT);
    pinMode(MOTOR_2, OUTPUT);

    softPwmCreate(MOTOR_PWM, MIN_SPEED, MAX_SPEED);
    /* sonic  */
    //pinMode(SIDE_TRIG, OUTPUT);
    //pinMode(SIDE_ECHO, INPUT);
    //
    pinMode(LED1_OBS, OUTPUT);
    pinMode(LED2_OBS, OUTPUT);

}

int speed_control(int value){
    if(value >= 0 && value <= MAX_SPEED)   {
        softPwmWrite(MOTOR_PWM, value);
    }else
        return ERR;
    return 0;
}

void motor_control(int control){
    if(control == GO){
        digitalWrite(MOTOR_1, HIGH);
        digitalWrite(MOTOR_2, LOW);
    }else if(control == STOP){
        digitalWrite(MOTOR_1, LOW);
        digitalWrite(MOTOR_2, LOW);
    }else if(control == BACK){
        digitalWrite(MOTOR_1, LOW);
        digitalWrite(MOTOR_2, HIGH);
    }
        
}

void red_light_handler(int signo){
    printf("STOP\n");
    motor_control(STOP);
    speed_control(0);
}

void green_light_handler(int signo){
    printf("GO\n");
    motor_control(GO);
    speed_control(DEFAULT_SPEED);
}

int motor_start(){
    FILE *fd;
    int retval;
    int ret=FAIL;

    fd = popen("sudo ./motor", "r");
    if(fd == NULL)
        return ret;

    retval = system("sudo ./motor");
    if(retval != 0){
        return ret;
    }

    ret = SUCCESS; 

    return ret;
}

void speed_up_handler(){
    speed_control(MAX_SPEED);
    printf("speed up\n");
}

void speed_down_handler(){
    speed_control(DEFAULT_SPEED);
    printf("speed down\n");
}
/*
void led_obs_handler(int signo){

    digitalWrite(LED1_OBS, HIGH);
    digitalWrite(LED2_OBS, HIGH);
    delay(1000);
    digitalWrite(LED1_OBS, LOW);
    digitalWrite(LED2_OBS, LOW);
    delay(1000);

}
*/
int main(){
    int pid, ret=1;
    int size=0;
    struct sigaction spd_up,spd_down;
    struct sigaction green, red;

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


    setup();        
    motor_control(GO);
    speed_control(DEFAULT_SPEED);

    spd_up.sa_handler = speed_up_handler;
    sigemptyset(&spd_up.sa_mask);
    spd_up.sa_flags=0;
    sigaction(61,&spd_up,0);

    spd_down.sa_handler = speed_down_handler;
    sigemptyset(&spd_down.sa_mask);
    spd_down.sa_flags=0;
    sigaction(62,&spd_down,0);

    green.sa_handler = green_light_handler;
    sigemptyset(&green.sa_mask);
    green.sa_flags=0;
    sigaction(59,&green,0);

    red.sa_handler = red_light_handler;
    sigemptyset(&red.sa_mask);
    red.sa_flags=0;
    sigaction(60,&red,0);

    /*
    led_obs.sa_handler = led_obs_handler;
    sigemptyset(&led_obs.sa_mask);
    led_obs.sa_flags=0;
    sigaction(58,&led_obs,0);
    */
    while(1){
        
    }
    
    return 0;
}
