#ifndef WATKIKLIENTOW_H
#define WATKIKLIENTOW_H

#include <iostream>
#include <boost/thread.hpp>
#include <boost/array.hpp>
#include <boost/asio.hpp>
#include <vector>

class WatkiKlientow
{
    public:
        WatkiKlientow(boost::asio::ip::tcp::socket* gniazdo);
        virtual ~WatkiKlientow();
        void napisz(std::string wiadomosc);
    protected:
    private:
        WatkiKlientow();
        boost::asio::ip::tcp::socket* gniazdo;

        //zmienne dotycz¹ce w¹tku;
        boost::thread *watekRun;

        void run();
};

#endif // WATKIKLIENTOW_H
