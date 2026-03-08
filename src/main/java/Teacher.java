class Teacher extends Person {
    private String position;
    private Department department;

    public Teacher(String name, String surname, String position, Department department) {
        super(name, surname);
        this.position = position;
        this.department = department;
    }

    public String getPosition() {return position;}

    @Override
    public String toString() {
        return getFullName() + " | Position: " + position + " | Department: " + department.getName();
    }
    @Override
    public String getDisplayInfo(){
        return toString();
    }

    public Department getDepartment() {
        return department;
    }
}