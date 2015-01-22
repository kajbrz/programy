#include "KlientTCP.h"

KlientTCP::KlientTCP()
{
    ip = "";
    port ="";
    //ctor
}

KlientTCP::~KlientTCP()
{
    stop();
}

void KlientTCP::start()
{
    gniazdo = new boost::asio::ip::tcp::socket(my_io_service);

    boost::asio::ip::tcp::resolver my_resolver(my_io_service);
    boost::asio::ip::tcp::resolver::query query(ip,port);

    boost::asio::ip::tcp::resolver::iterator listaPunktow(my_resolver.resolve(query));

    try
    {
        boost::asio::connect(*gniazdo, listaPunktow);
        wlaczony = true;
        watek = new boost::thread(&KlientTCP::run, this);
        std::cout << "Polaczono" << std::endl;
    }
    catch(std::exception ex)
    {
        std::cout << "Nie udalo sie polaczyc" << std::endl;
    }
}
void KlientTCP::stop()
{
    std::cout << "Zamykanie gniazda..." << std::endl;
    wlaczony = false;
    if(gniazdo != nullptr)
        gniazdo->cancel();
    delete gniazdo;

    std::cout << "Wylaczenie odbierania danych" << std::endl;
    if(watek != nullptr)
        watek->join();
    delete watek;
}
void KlientTCP::ustawIP(std::string ip)
{
    this->ip = ip;
}
void KlientTCP::ustawPort(std::string port)
{
    this->port = port;
}

void KlientTCP::napisz(std::string wiadomosc)
{
    if(wlaczony)
        gniazdo->send(boost::asio::buffer(wiadomosc, 128));
}

void KlientTCP::run() //metoda w¹tku
{
    napisz("Witam! Z tej strony klient");

    while(wlaczony)
    {
        boost::array<char, 128> buf;
        boost::system::error_code error;
        size_t len = 0;
        try
        {
            len = gniazdo->read_some(boost::asio::buffer(buf), error);
        }
        catch(std::exception e)
        {
            std::cout << "Wylaczanie" << std::endl;
            wlaczony = false;
        }
        if (len > 2)
        {
            std::cout << "Odebrano od serwera: ";
            std::cout.write(buf.data(), len);
            std::cout << std::endl;
        }
    }
}
