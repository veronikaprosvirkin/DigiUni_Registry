class Teacher extends Person {
    private String position;

    public Teacher(String name, String surname, String position) {
        super(name, surname);
        this.position = position;
    }

    public String getPosition() {return position;}

    @Override
    public String toString() {
        return "Teacher: " + getFullName() + " | Position: " + position;
    }
}