package commandpattern;

public class Main {

    public static void main(String[] args) {

        /* write your code here */
        Broker broker = new Broker();
        Stock stock = new Stock();
        Command buyCommand = new BuyCommand(stock);

        broker.setCommand(buyCommand);
        broker.executeCommand();

        Command sellCommand = new SellCommand(stock);

        broker.setCommand(sellCommand);
        broker.executeCommand();
    }
}


class Stock {

    public void buy() {
        System.out.println("Stock was bought");
    }

    public void sell() {
        System.out.println("Stock was sold");
    }
}

interface Command {
    void executeCommand();
    /* write your code here */
}

class BuyCommand implements Command {
    private Stock stock;

    public BuyCommand(Stock stock) {
        this.stock = stock;
    }

    @Override
    public void executeCommand() {
        stock.buy();
    }

    /* write your code here */
}

class SellCommand implements Command {
    private Stock stock;

    public SellCommand(Stock stock) {
        this.stock = stock;
    }

    @Override
    public void executeCommand() {
        stock.sell();
    }

    /* write your code here */
}

class Broker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.executeCommand();
        /* write your code here */
    }
}