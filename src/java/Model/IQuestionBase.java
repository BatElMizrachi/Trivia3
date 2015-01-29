package Model;

interface IQuestionBase {
    public Category getCategory();
    public void setCategory(Category category);
    public Level getLevel();
    public void setLevel(Level level);
    public int getCode();
    public void setCode(int code);
}
