class Teacher extends Person {
    private String position;
    private Department department;
    private String id;

    public Teacher(String id, String name, String surname, String patronymic, String position, Department department) {
        super(name, surname, patronymic);
        this.position = position;
        this.department = department;
        this.id = id;
    }

    public String getId(){return id;}

    public String getPosition() {return position;}
    public void setPosition(String position) {this.position = position;}

    public Department getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        String deptName;

        if (this.department != null) {
            deptName = this.department.getName();
        } else {
            deptName = "No Department Assigned";
        }

        return getFullName() + " | Position: " + position + " | Department: " + deptName;
    }

    @Override
    public String getDisplayInfo(){
        return toString();
    }
}