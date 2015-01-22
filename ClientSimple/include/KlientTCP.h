#ifndef KLIENTTCP_H
#define KLIENTTCP_H

#include <iostream>
#include <boost/thread.hpp>
#include <boost/asio.hpp>
#include <boost/array.hpp>

class KlientTCP
{
    public:
        KlientTCP();
        virtual ~KlientTCP();

        void start();
        void stop();
        void ustawIP(std::string ip);
        void ustawPort(std::string port);

        void napisz(std::string wiadomosc);
    protected:

    private:
        boost::asio::ip::tcp::socket *gniazdo = nullptr;
        boost::asio::io_service my_io_service;
        std::string ip;
        std::string port;
        //
        bool wlaczony;
        boost::thread *watek = nullptr;
        void run(); //funkcja watku;
};

#endif // KLIENTTCP_H
