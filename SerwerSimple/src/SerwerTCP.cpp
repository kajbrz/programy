#include "SerwerTCP.h"

SerwerTCP::SerwerTCP()
{
    std::cout << "Zainicjowano serwer.. " << std::endl;
    port = 3310; //default port
    wlaczony = false;
    watekRun = nullptr;
}

SerwerTCP::~SerwerTCP()
{
    if(wlaczony == true)
        stop();
    std::cout << "Wylaczanie serwera.. " << std::endl;
    watekRun->join();
    delete watekRun;
    std::cout << "Wylaczono.. " << std::endl;
}

void SerwerTCP::setPort(int port)
{
    this->port = port;
}
void SerwerTCP::start()
{
    std::cout << "Wlaczanie serwera.. " << std::endl;
    wlaczony = true;
    watekRun = new boost::thread(&SerwerTCP::run, this);
    std::cout << "Wlaczono serwer.. " << std::endl;

    std::cout << "Wlaczanie nadawania stacji" << std::endl;
    boost::thread(&SerwerTCP::runWypisywanie, this);
}

void SerwerTCP::stop()
{

    std::cout << "Zatrzymywanie serwera.. " << std::endl;
    wlaczonyMtx.lock();
    wlaczony = false;
    wlaczonyMtx.unlock();
    myAcceptor->cancel();
    std::cout << "Zatrzymano serwer.. " << std::endl;
}

void SerwerTCP::napiszDoWszystkich(std::string wiadomosc)
{
    if (watkiKlientow.size() > 0)
    {
        for (WatkiKlientow *watek : watkiKlientow)
        {
            watek->napisz(wiadomosc);
        }
    }
}

void SerwerTCP::runWypisywanie()
{
    while(wlaczony)
    {
        napiszDoWszystkich("Nadajemy radiostacje\n");
    }
}

void SerwerTCP::napiszDoKlienta(std::string wiadomosc, int klient)
{
    if(klient >= 0 && klient < watkiKlientow.size())
    {
        watkiKlientow[klient]->napisz(wiadomosc);
    }
}



void SerwerTCP::run() //w¹tek serwera
{
    myAcceptor = new boost::asio::ip::tcp::acceptor(my_io_service,
                                                    boost::asio::ip::tcp::endpoint(
                                                        boost::asio::ip::tcp::v4(), port));

    std::cout << "Stworzenie nowego gniazda.. " << std::endl;
    while(true)
    {
        boost::asio::ip::tcp::socket *noweGniazdo
            = new boost::asio::ip::tcp::socket(myAcceptor->get_io_service());

        try
        {

            myAcceptor->accept(*noweGniazdo);
            std::cout << "Nawiazano polaczenie.. " << std::endl;
            utworzWatek(noweGniazdo);
        }
        catch(boost::system::system_error e)
        {

            std::cout << "Przerwano nas³uchiwanie.. " << std::endl;
            delete noweGniazdo;
            break;
        }


        wlaczonyMtx.lock();
        if(!wlaczony)
            break;
        wlaczonyMtx.unlock();
    }
    delete myAcceptor;

}

void SerwerTCP::utworzWatek(boost::asio::ip::tcp::socket *noweGniazdo)
{

    watkiKlientow.push_back(new WatkiKlientow(noweGniazdo));
}
