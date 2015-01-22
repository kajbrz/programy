

#include <iostream>
#include <conio.h>
#include <thread>
#include <ctime>

using namespace std;

bool koniec = false;
void dupa()
{
    static time_t czas = time(0);
    while(!koniec)
    {
        if ((time(0) - czas) > 2)
        {
            czas = time(0);
            cout << "Siema";
        }

    }
}

int main()
{
    thread wyswietlanie(dupa);

    while(!koniec)
    {
        cout << "Podaj znak: ";
        char znak = getch();

        if((int)znak > '0')
            koniec = true;
    }

    wyswietlanie.join();
}
