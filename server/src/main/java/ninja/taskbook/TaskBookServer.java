package ninja.taskbook;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;

import ninja.taskbook.hello.HelloService;

public class TaskBookServer {
    public static final int SERVER_PORT = 8090;

    public void startServer() {
        try {
            System.out.println("Server start ...");

            TProcessor processor = new HelloService.Processor<HelloService.Iface> (new HelloServiceImpl());
            TServerSocket transport = new TServerSocket(SERVER_PORT);
            TThreadPoolServer.Args args = new TThreadPoolServer.Args(transport);
            args.processor(processor);
            args.protocolFactory(new TBinaryProtocol.Factory());

            TServer server = new TThreadPoolServer(args);
            server.serve();

        } catch (Exception e) {
            System.out.println("Server start error ...");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TaskBookServer server = new TaskBookServer();
        server.startServer();
    }

    public class HelloServiceImpl implements HelloService.Iface {

        public HelloServiceImpl() {
        }

        @Override
        public int hi(String word1, String word2, String word3) throws TException {
            System.out.println("hi:" + word1 + word2 + word3);
            return 123;
        }

    }
}
