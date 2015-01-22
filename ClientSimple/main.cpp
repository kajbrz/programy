#include <iostream>
#include "KlientTCP.h"

#include <chrono>
#include <thread>
using namespace std;

/*
    KLIENT
*/

int main()
{
    KlientTCP klientTCP;
    klientTCP.ustawIP(std::string("localhost"));
    klientTCP.ustawPort(std::string("15"));

    klientTCP.start();


    std::this_thread::sleep_for(std::chrono::milliseconds(10000));

}









