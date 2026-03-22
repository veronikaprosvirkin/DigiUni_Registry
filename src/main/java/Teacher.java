class Teacher extends Person {
    private String position;
    private Department department;
    private String id;

    public Teacher(String id, String name, String surname, String position, Department department) {
        super(name, surname);
        this.position = position;
        this.department = department;
        this.id = id;
    }
    public String getId(){return id;}


    public String getPosition() {return position;}
    public void setPosition(String position) {this.position = position;}

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