import java.util.Objects;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {

    private final Coach coach;
    private int count;

    public CounterOfTrainings(Coach coach) {
        this.coach = coach;
        this.count = 1;
    }

    public void increaseCountByOne() {
        count++;
    }

    @Override
    public int compareTo(CounterOfTrainings o) {
        // 1. сортировка по убыванию количества тренировок
        int diff = Integer.compare(o.count, this.count);
        if (diff != 0) return diff;

        // 2. если количество совпадает, сортируем по ФИО
        diff = this.coach.getSurname().compareTo(o.coach.getSurname());
        if (diff != 0) return diff;

        diff = this.coach.getName().compareTo(o.coach.getName());
        if (diff != 0) return diff;

        return this.coach.getMiddleName().compareTo(o.coach.getMiddleName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CounterOfTrainings that = (CounterOfTrainings) o;
        return count == that.count && Objects.equals(coach, that.coach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coach, count);
    }

    @Override
    public String toString() {
        return coach +
                ", количество тренировок: " + count +
                '}';
    }

    public int getCount() {
        return count;
    }
}
