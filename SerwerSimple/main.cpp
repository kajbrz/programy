#include <iostream>
#include <boost/thread.hpp>
#include <boost/asio.hpp>
#include <boost/chrono.hpp>

using namespace boost::asio;
using namespace boost;

/*
    SERWER
*/
#include "SerwerTCP.h"
int main()
{
    SerwerTCP serwerTcp;

    serwerTcp.setPort(15);
    serwerTcp.start();
    boost::this_thread::sleep_for(boost::chrono::milliseconds(10000));
    serwerTcp.stop();

    serwerTcp.napiszDoWszystkich("Koncymy panowie");
}
