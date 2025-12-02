import java.util.*;


public class Timetable {
    private Map<DayOfWeek, Map<TimeOfDay, Set<TrainingSession>>> timetable = new HashMap<>();

    Comparator<TimeOfDay> timeOfDayComparator = new Comparator<>() {
        @Override
        public int compare(TimeOfDay t1, TimeOfDay t2) {
            //Сравним часы
            int diff = Integer.compare(t1.getHours(), t2.getHours());
            if (diff != 0) return diff;

            //Если часы равны, то идем по убыванию к более мелким сущностям. Далее минуты
            diff = Integer.compare(t1.getMinutes(), t2.getMinutes());
            return diff;
        }
    };

    public boolean addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        Map<TimeOfDay, Set<TrainingSession>> mapTrainingSessionsOfDay;
        Set<TrainingSession> setTrainingSessionsOfDay;
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();

        if (timetable.containsKey(dayOfWeek)) {
            mapTrainingSessionsOfDay = timetable.get(dayOfWeek);
        } else {
            mapTrainingSessionsOfDay = new TreeMap<>(timeOfDayComparator);
            timetable.put(dayOfWeek, mapTrainingSessionsOfDay);
        }

        if (mapTrainingSessionsOfDay.containsKey(timeOfDay)) {
            setTrainingSessionsOfDay = mapTrainingSessionsOfDay.get(timeOfDay);
            for (TrainingSession thisTrainingSession : setTrainingSessionsOfDay) {
                if (thisTrainingSession.getCoach().equals(trainingSession.getCoach())
                        || thisTrainingSession.getGroup().equals(trainingSession.getGroup())) {
                    return false;
                }
            }
        } else {
            setTrainingSessionsOfDay = new HashSet<>();
        }
        setTrainingSessionsOfDay.add(trainingSession);
        mapTrainingSessionsOfDay.put(timeOfDay, setTrainingSessionsOfDay);

        return true;
    }


    public Map<TimeOfDay, Set<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        //сложность должна быть О(1)
        return timetable.get(dayOfWeek);

    }

    public Set<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //сложность должна быть О(1)
        Map<TimeOfDay, Set<TrainingSession>> trainingSessionsOfDay;
        trainingSessionsOfDay = timetable.get(dayOfWeek);
        if (trainingSessionsOfDay != null) {
            return trainingSessionsOfDay.get(timeOfDay);
        } else {
            return null;
        }
    }

    public Set<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, CounterOfTrainings> mapCountByCoaches = new HashMap<>();
        for (Map.Entry<DayOfWeek, Map<TimeOfDay, Set<TrainingSession>>> entry : timetable.entrySet()) {
            for (Map.Entry<TimeOfDay, Set<TrainingSession>> entry1 : entry.getValue().entrySet()) {
                for (TrainingSession trainingSession : entry1.getValue()) {
                    Coach coach = trainingSession.getCoach();
                    if (mapCountByCoaches.containsKey(coach)) {
                        mapCountByCoaches.get(coach).increaseCountByOne();
                    } else {
                        mapCountByCoaches.put(coach, new CounterOfTrainings(coach));
                    }
                }
            }
        }
        Set<CounterOfTrainings> counterOfTrainings = new TreeSet<>(mapCountByCoaches.values());
        return counterOfTrainings;
    }
}
