package commandpattern.pattern;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int[] amountList = new int[3];

        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            amountList[i] = scanner.nextInt();
        }

        Broker broker = new Broker();
        for (int i = 0; i < 3; i++) {
            Option option = new Option(amountList[i]);
            Command command;
            if (amountList[i] > 0) {
                command = new BuyCommand(option);
                /* write your code here */
            } else {
                command = new SellCommand(option);
                /* write your code here */
            }
            broker.setCommand(command);
            broker.executeCommand();
        }
    }
}

class Option {
    private int amount;

    Option(int amount) {
        this.amount = amount;
    }

    void buy() {
        System.out.println(amount + " was bought");
    }

    void sell() {
        System.out.println(amount + " was sold");

        /* write your code here */
    }
}

interface Command {
    void execute();
    /* write your code here */
}

class BuyCommand implements Command {
    private Option option;

    BuyCommand(Option option) {
        this.option = option;
    }

    @Override
    public void execute() {
        option.buy();
    }

    /* write your code here */
}

class SellCommand implements Command {
    private Option option;

    SellCommand(Option option) {
        this.option = option;
    }

    @Override
    public void execute() {
        option.sell();
    }

    /* write your code here */
}

class Broker {
    private Command command;

    void setCommand(Command command) {
        this.command = command;
        /* write your code here */
    }

    void executeCommand() {
        command.execute();
        /* write your code here */
    }
}
