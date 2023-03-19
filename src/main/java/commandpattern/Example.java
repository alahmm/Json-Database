package commandpattern;

import java.util.Scanner;

class Client {

    public static void main(String[] args) {

        Controller controller = new Controller();
        TV tv = new TV();

        int[] channelList = new int[3];

        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            channelList[i] = scanner.nextInt();
        }

        Command2 turnOnTV = new TurnOnCommand(tv);
        turnOnTV.execute();
        /* write your code here */

        Command2 changeChannel;
        for (int i = 0; i < 3; i++) {
            Channel channel = new Channel(tv, channelList[i]);
            channel.changeChannel();

            /* write your code here */
        }

        Command2 turnOffTV = new TurnOffCommand(tv);
        turnOffTV.execute();
        /* write your code here */
    }
}

class TV {

    Channel channel;

    void turnOn() {
        System.out.println("Turning on the TV");
        setChannel(new Channel(this, 0));
    }

    void turnOff() {
        System.out.println("Turning off the TV");
        /* write your code here */
    }

    void setChannel(Channel channel) {
        this.channel = channel;
    }
}

class Channel {
    private TV tv;
    private int channelNumber;

    Channel(TV tv, int channelNumber) {
        this.tv = tv;
        this.channelNumber = channelNumber;
        /* write your code here */
    }

    void changeChannel() {
        tv.setChannel(this);
        System.out.println("Channel was changed to " + channelNumber);
    }
}

interface Command2 {
    void execute();
    /* write your code here */
}

class TurnOnCommand implements Command2 {
    private TV tv;
    /* write your code here */

    TurnOnCommand(TV tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.turnOn();
        /* write your code here */
    }
}

class TurnOffCommand implements Command2 {
    private TV tv;
    /* write your code here */

    TurnOffCommand(TV tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.turnOff();
        /* write your code here */
    }
}

class ChangeChannelCommand implements Command2 {

    private Channel channel;

    ChangeChannelCommand(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void execute() {
        channel.changeChannel();
    }

    /* write your code here */
}

class Controller {
    private Command2 command;

    void setCommand(Command2 command) {
        this.command = command;
    }

    void executeCommand() {
        command.execute();
        /* write your code here */
    }
}

