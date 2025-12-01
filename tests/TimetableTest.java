//import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.*;

public class TimetableTest {

    private static Timetable timetable;

    @BeforeEach
    void beforeEach() {
        timetable = new Timetable();
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size(),
                "Вернулось более одного занятия");
        //Проверить, что за вторник не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY),
                "Вторник не пустой");
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, Set<TrainingSession>> trainingSessionsOfDay;
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size(),
                "Вернулось более одного занятия в понедельник");

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        trainingSessionsOfDay = (TreeMap<TimeOfDay, Set<TrainingSession>>) timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        int actualSessions = 0;
        if (trainingSessionsOfDay != null){
            actualSessions = trainingSessionsOfDay.size();
            if (actualSessions > 1){
               Map.Entry<TimeOfDay, Set<TrainingSession>> entry = trainingSessionsOfDay.pollFirstEntry();
                Assertions.assertEquals(13, entry.getKey().getHours(),
                        "Неверное время первого занятия в четверг");
                entry = trainingSessionsOfDay.pollFirstEntry();
                Assertions.assertEquals(20, entry.getKey().getHours(),
                        "Неверное время второго занятия в четверг");
            }
        }
        Assertions.assertEquals(2, actualSessions, "Неверное количество занятий в четверг");

        // Проверить, что за вторник не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY),
                "Неверное количество занятий во вторник");
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        Set<TrainingSession> setTrainingSessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));

        Assertions.assertNotNull(setTrainingSessions, "Неверное количество занятий в 13:00");

        if (setTrainingSessions != null) {
            Assertions.assertEquals(1,setTrainingSessions.size(),
                    "Неверное количество занятий в 13:00");
        }

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        setTrainingSessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));
        Assertions.assertNull(setTrainingSessions,"Неверное количество занятий в 14:00");

    }



    @Test
    void testGetTrainingSessionsForDayAndTimeAtHours() {

        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Плавание для детей", Age.CHILD, 60);
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Петров", "Петр", "Николаевич");
        TrainingSession singleTrainingSession1 = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession singleTrainingSession2 = new TrainingSession(group2, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession1);
        timetable.addNewTrainingSession(singleTrainingSession2);

        //Проверить, что за понедельник в 13:00 вернулось два занятия
        Set<TrainingSession> setTrainingSessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));

        Assertions.assertNotNull(setTrainingSessions, "Неверное количество занятий в 13:00");

        if (setTrainingSessions != null) {
            Assertions.assertEquals(2,setTrainingSessions.size(),
                    "Неверное количество занятий в 13:00");
        }
    }

    @Test
    void testAddNewTrainingSessionOneCoachOneTime() {
        //Один тернер не может вести занятия у разных групп в одно время в один день
        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Плавание для детей", Age.CHILD, 60);
        Coach coach1 = new Coach("Петров", "Петр", "Николаевич");
        Coach coach2 = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession1coach1 = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession singleTrainingSession2coach1 = new TrainingSession(group2, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession singleTrainingSession3coach2 = new TrainingSession(group2, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));


        Assertions.assertTrue(timetable.addNewTrainingSession(singleTrainingSession1coach1),
                "Тренировка должна быть добавлена");
        Assertions.assertFalse(timetable.addNewTrainingSession(singleTrainingSession2coach1),
                "Тренировка НЕ должна быть добавлена");
        Assertions.assertTrue(timetable.addNewTrainingSession(singleTrainingSession3coach2),
                "Тренировка должна быть добавлена");

    }

    @Test
    void testAddNewTrainingSessionOneGroupOneTime() {
        //У одной группы не могут быть занятия в одно время в один день
        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);

        Coach coach1 = new Coach("Петров", "Петр", "Николаевич");
        Coach coach2 = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession1 = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession singleTrainingSession2= new TrainingSession(group1, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession singleTrainingSession3= new TrainingSession(group1, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));


        Assertions.assertTrue(timetable.addNewTrainingSession(singleTrainingSession1),
                "Тренировка должна быть добавлена");
        Assertions.assertFalse(timetable.addNewTrainingSession(singleTrainingSession2),
                "Тренировка НЕ должна быть добавлена");
        Assertions.assertTrue(timetable.addNewTrainingSession(singleTrainingSession3),
                "Тренировка должна быть добавлена");

    }



    @Test
    void testCounterOfTrainingsTopCoach() {

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach1 = new Coach("Васильев1", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Васильев2", "Николай", "Сергеевич");
        Coach coach3 = new Coach("Васильев3", "Николай", "Сергеевич");

        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.TUESDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.FRIDAY, new TimeOfDay(13, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(13, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.FRIDAY, new TimeOfDay(13, 0)));

        TreeSet <CounterOfTrainings> setCounterOfTrainings
                = (TreeSet<CounterOfTrainings>) timetable.getCountByCoaches();

        Assertions.assertEquals(5, setCounterOfTrainings.first().getCount(),
                "Неверное значение максимальное количества тренировок у тренера");
    }

    @Test
    void testCounterOfTrainingsLastCoach() {

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach1 = new Coach("Васильев1", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Васильев2", "Николай", "Сергеевич");
        Coach coach3 = new Coach("Васильев3", "Николай", "Сергеевич");

        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.TUESDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.FRIDAY, new TimeOfDay(13, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(14, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.MONDAY, new TimeOfDay(15, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.FRIDAY, new TimeOfDay(15, 0)));

        TreeSet <CounterOfTrainings> setCounterOfTrainings
                = (TreeSet<CounterOfTrainings>) timetable.getCountByCoaches();

        Assertions.assertEquals(2, setCounterOfTrainings.last().getCount(),
                "Неверное значение минимального количества тренировок у тренера");
    }

    @Test
    void testCounterOfTrainingsCoachIsNotFound() {

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach1 = new Coach("Васильев1", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Васильев2", "Николай", "Сергеевич");

        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.TUESDAY, new TimeOfDay(13, 0)));


        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(13, 0)));


        TreeSet <CounterOfTrainings> setCounterOfTrainings
                = (TreeSet<CounterOfTrainings>) timetable.getCountByCoaches();

        Assertions.assertFalse(setCounterOfTrainings.contains(
                new CounterOfTrainings(new Coach("Васильев3", "Николай", "Сергеевич"))),
                "Тренер не проводивший тренировки не найден во множестве");
    }

    @Test
    void testCounterOfTrainingsCoachIsFound() {

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach1 = new Coach("Васильев1", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Васильев2", "Николай", "Сергеевич");

        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.TUESDAY, new TimeOfDay(13, 0)));


        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(14, 0)));


        TreeSet <CounterOfTrainings> setCounterOfTrainings
                = (TreeSet<CounterOfTrainings>) timetable.getCountByCoaches();

        Assertions.assertTrue(setCounterOfTrainings.contains(
                        new CounterOfTrainings(new Coach("Васильев2", "Николай", "Сергеевич"))),
                "Тренер проводивший тренировки не найден во множестве");
    }


}
