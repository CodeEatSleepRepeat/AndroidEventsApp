package rs.ac.uns.ftn.eventsapp.utils;

import android.net.Uri;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.models.Event;
import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.Friendship;
import rs.ac.uns.ftn.eventsapp.models.FriendshipStatus;
import rs.ac.uns.ftn.eventsapp.models.Invitation;
import rs.ac.uns.ftn.eventsapp.models.InvitationStatus;
import rs.ac.uns.ftn.eventsapp.models.User;

public class TestMockup {

    public static final ArrayList<Event> events = new ArrayList<>();
    public static final ArrayList<User> users = new ArrayList<>();
    public static final ArrayList<Invitation> invitations = new ArrayList<>();


    private static final TestMockup ourInstance = new TestMockup();

    public static TestMockup getInstance() {
        return ourInstance;
    }

    private TestMockup() {
        User u1 = new User(1l, "Borko Bakic", "baki9@example.com", "sdcsdc84sSA", "SD58s4dSD8s87sdfSf4sdf87sdf48SDFsd178", null, null, null, null, null, null, null, true);
        User u2 = new User(2l, "Martin Kovac", "kovac.m@example.com", "sdcsdcs8794", "1s4asSDF894FSDSDF478sd48fsdfsdf8s74ssd", null, null, null, null, null, null, null, true);
        User u3 = new User(3l, "Aleksej Stralic", "ftnstudent@example.com", "sdf984sd54", "aiushdASDOF#EP#EKC#K$Rc#R$cCWepkcweC44c", null, null, null, null, null, null, null, true);
        addExtraUsers();

        Friendship f1 = new Friendship(1l, u1, u2, FriendshipStatus.ACCEPTED);
        Friendship f2 = new Friendship(2l, u3, u1, FriendshipStatus.ACCEPTED);
        Friendship f3 = new Friendship(3l, u2, u3, FriendshipStatus.PENDING);

        ArrayList<Friendship> u1Send = new ArrayList<>();
        ArrayList<Friendship> u2Send = new ArrayList<>();
        ArrayList<Friendship> u3Send = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 0, 11, 17, 0, 0);    //mesec pocinje od 0 -> januar!?
        Date startDate = calendar.getTime();
        calendar.set(2020, 9, 11, 23, 30, 0);
        Date endDate = calendar.getTime();

        Event e1 = new Event(1l, "Student day party", "Student day must be celebrated! Come join us and have fun! :)", "http://www.mojnovisad.com/files/news/3/8/5/11385/11385-dsc-7619.jpg", EventType.PARTY, true, startDate, endDate, "Mr. Worlwide", 45l, 20l, u1, null, null, null);
        Event e2 = new Event(2l, "Android programing day", "Just program. No party!", "https://i.pinimg.com/originals/9b/c0/31/9bc031ded28a4eccb4a3f1df621ff84d.png", EventType.HANGING, true, startDate, endDate, "My home address", 41l, 74l, u2, null, null, null);

        Invitation i1 = new Invitation(1l, u1, u2, e1, InvitationStatus.ACCEPTED);
        Invitation i2 = new Invitation(1l, u2, u3, e1, InvitationStatus.ACCEPTED);
        Invitation i3 = new Invitation(1l, u3, u1, e2, InvitationStatus.PENDING);
        invitations.add(i1);
        invitations.add(i2);
        invitations.add(i3);

        ArrayList<Invitation> u1Inv = new ArrayList<>();
        ArrayList<Invitation> u2Inv = new ArrayList<>();
        ArrayList<Invitation> u3Inv = new ArrayList<>();

        ArrayList<Event> u1e1 = new ArrayList<>();
        ArrayList<Event> u2e2 = new ArrayList<>();

        //friendship requests
        u1Send.add(f1);
        u1.setSendRequests(u1Send);
        u2.setReceivedRequests(u1Send);

        u2Send.add(f3);
        u2.setSendRequests(u2Send);
        u3.setReceivedRequests(u2Send);

        u3Send.add(f2);
        u3.setSendRequests(u3Send);
        u1.setReceivedRequests(u3Send);

        //invitations
        u1Inv.add(i1);
        u2Inv.add(i2);
        u3Inv.add(i3);

        u1.setSendInvitations(u1Inv);
        u2.setSendInvitations(u2Inv);
        u3.setSendInvitations(u3Inv);
        u2.setReceivedInvitations(u1Inv);
        u3.setReceivedInvitations(u2Inv);
        u1.setReceivedInvitations(u3Inv);

        //events
        u1e1.add(e1);
        u2e2.add(e2);
        u1.setUserEvents(u1e1);
        u2.setUserEvents(u2e2);

        u1.setGoingEvents(u2e2);
        u2.setInterestedEvents(u1e1);
        u3.setInterestedEvents(u1e1);
        u3.setGoingEvents(u2e2);

        //final 2 lists that are accessible from this class
        events.add(e1);
        events.add(e2);

        for (int i = 3; i < 50; i++) {
            if (i % 3 == 0) {
                events.add(new Event(new Long(i), "Student day party", "Student day must be celebrated! Come join us and have fun! :)", "http://www.mojnovisad.com/files/news/3/8/5/11385/11385-dsc-7619.jpg", EventType.PARTY, true, startDate, endDate, "Mr. Worlwide", 5645415l, 4517l, u1, null, null, null));
            } else {
                events.add(new Event(new Long(i), "Android programing day", "Just program. No party!", "https://i.pinimg.com/originals/9b/c0/31/9bc031ded28a4eccb4a3f1df621ff84d.png", EventType.HANGING, true, startDate, endDate, "My home address", 12478773l, 23454147l, u2, null, null, null));
            }
        }
        users.add(u1);
        users.add(u2);
        users.add(u3);
    }

    private void addExtraUsers(){
        User u4 = new User(4l, "Branko", "branko_ftn@example.com", "123456",
                "aiushdASDOF#EP#EKC#K$Rc#R$cCWepkcweC44c", null, null, null, null, null, null, null, true);
        u4.setProfileImageUrl("https://i1.rgstatic.net/ii/profile.image/277011466604544-1443056088013_Q512/Branko_Perisic.jpg");
        User u5 = new User(5l, "Krilin", "krilin@example.com", "123456",
                "aiushdASDOF#EP#EKC#K$Rc#R$cCWepkcweC44c", null, null, null, null, null, null, null, true);
        u5.setProfileImageUrl("https://i.ytimg.com/vi/sZlzNX0x-v8/maxresdefault.jpg");
        User u6 = new User(6l, "Rick", "rick@example.com", "123456",
                "aiushdASDOF#EP#EKC#K$Rc#R$cCWepkcweC44c", null, null, null, null, null, null, null, true);
        u6.setProfileImageUrl("https://pbs.twimg.com/profile_images/1150075184264691712/3VkPTord_400x400.png");
        User u7 = new User(7l, "Karlos", "karlos_slagalica@example.com", "123456",
                "aiushdASDOF#EP#EKC#K$Rc#R$cCWepkcweC44c", null, null, null, null, null, null, null, true);
        u7.setProfileImageUrl("https://xdn.tf.rs/2020/04/06/karlos-franko-slagalica-830x553.jpg");
        User u8 = new User(8l, "Nole", "nole_srbin@example.com", "123456",
                "aiushdASDOF#EP#EKC#K$Rc#R$cCWepkcweC44c", null, null, null, null, null, null, null, true);
        u8.setProfileImageUrl("https://www.politikaplus.com/img/s/810x500/upload/images/arhiva/N/novak-djokovic-100.jpg");
        User u9 = new User(9l, "Vule", "vule_srbin@example.com", "123456",
                "aiushdASDOF#EP#EKC#K$Rc#R$cCWepkcweC44c", null, null, null, null, null, null, null, true);
        u9.setProfileImageUrl("https://m.cdm.me/wp-content/uploads/2020/04/Aleksandar-Vu%C4%8Di%C4%87-foto-predsednilk.rs-min-340x204.jpg");
        User u10 = new User(10l, "Tyrion", "tyrion@example.com", "123456",
                "aiushdASDOF#EP#EKC#K$Rc#R$cCWepkcweC44c", null, null, null, null, null, null, null, true);
        u10.setProfileImageUrl("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/theory-1553634761.jpg?crop=0.501xw:1.00xh;0,0&resize=480:*");
        User u11 = new User(11l, "L", "l@example.com", "123456",
                "aiushdASDOF#EP#EKC#K$Rc#R$cCWepkcweC44c", null, null, null, null, null, null, null, true);
        u11.setProfileImageUrl("https://vignette.wikia.nocookie.net/deathnote/images/8/83/Lawliet-L-Cole.png/revision/latest?cb=20170907105910");
        User u13 = new User(13l, "Tesa Tesanovic", "tesa_tesanovic" +
                "@example.com", "123456",
                "aiushdASDOF#EP#EKC#K$Rc#R$cCWepkcweC44c", null, null, null, null, null, null, null, true);
        u13.setProfileImageUrl("https://www.balkaninfo.rs/wp-content/uploads/2018/01/Te%C5%A1a-1024x795.jpg");

        users.add(u4);
        users.add(u5);
        users.add(u6);
        users.add(u7);
        users.add(u8);
        users.add(u9);
        users.add(u10);
        users.add(u11);
        users.add(u13);
    }
}
