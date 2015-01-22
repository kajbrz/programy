#include "WatkiKlientow.h"

WatkiKlientow::WatkiKlientow()
{

}

WatkiKlientow::WatkiKlientow(boost::asio::ip::tcp::socket *gniazdo)
{

    std::cout << "Inicjowanie nowego klienta.. " << std::endl;
    this->gniazdo = gniazdo;

    watekRun = new boost::thread(&WatkiKlientow::run, this);
}

WatkiKlientow::~WatkiKlientow()
{
    gniazdo->close();
    delete gniazdo;
    watekRun->join();
    delete watekRun;
}
void WatkiKlientow::napisz(std::string wiadomosc)
{
    if(gniazdo != nullptr)
    {
        try
        {
            gniazdo->send(boost::asio::buffer(wiadomosc,128));
        }
        catch (std::exception e)
        {
            std::cout << "Zerwano polaczenie" << std::endl;
            return;
        }
    }
}
void WatkiKlientow::run()
{
    boost::array<char, 128> buf;
    boost::system::error_code error;

    std::cout << "Nas³uchiwanie na linii klienta.. " << std::endl;
    while(true)
    {
        try
        {
            size_t length = gniazdo->read_some(boost::asio::buffer(buf), error);

            if(length < 2)
                continue;
            std::cout << "Odebrano od Klienta: ";
            std::cout.write(buf.data(), length);
            std::cout << std::endl;
            buf.begin();
        }
        catch(boost::system::system_error e)
        {
            std::cout << "Przerwano odczyt z gniada..." << std::endl;
            return;
        }
    }
}















