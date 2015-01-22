#include <iostream>
#include <boost/thread.hpp>
#include <boost/asio.hpp>
#include <vector>
#include "WatkiKlientow.h"

class SerwerTCP
{
public:
    SerwerTCP();
    virtual ~SerwerTCP();

    void setPort(int port);
    void start();
    void stop();

    void napiszDoWszystkich(std::string wiadomosc);
    void napiszDoKlienta(std::string wiadomosc, int klient);
private:
    std::vector<WatkiKlientow*> watkiKlientow;
    int port;
    boost::asio::io_service my_io_service;
    boost::asio::ip::tcp::acceptor *myAcceptor = nullptr;
    void run();
    void runWypisywanie();
    //zmienne dotycz¹ce w¹tka;
    boost::thread *watekRun;
    bool wlaczony;
    boost::mutex wlaczonyMtx;

    //metody prywatne
    void utworzWatek(boost::asio::ip::tcp::socket *noweGniazdo);
};
