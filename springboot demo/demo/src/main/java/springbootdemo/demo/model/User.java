package springbootdemo.demo.model;

public class User
{
    //e://春招项目/-Springboot-
   private int id;
   private String name;
   private String password;
   private String salt;
   private String headUrl;

   public User(String name)
   {
        this.name=name;
        this.password="";
        this.salt="";
        this.headUrl="";      
   }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

}