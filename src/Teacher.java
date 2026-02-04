class Teacher extends Person {
    private String position;

    public Teacher(String name, String position) {
        super(name);
        this.position = position;
    }

    public String getPosition() {return position;}

    @Override
    public String toString() {
        return "Teacher: " + getName() + " | Position: " + position;
    }
}