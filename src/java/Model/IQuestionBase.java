package Model;

interface IQuestionBase {
    public Category GetCategory();
    public void SetCategory(Category category);
    public Level GetLevel();
    public void SetLevel(Level level);
    public int GetCode();
    public void SetCode(int code);
}
