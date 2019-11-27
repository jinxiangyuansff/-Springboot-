package springbootdemo.demo.model;

public class Page 
  {
    private int current = 1;
  
    private int limit = 10;

    private int rows;

    // 查询路径(用于复用分页链接）
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /*
     获得当前起始行
    */
    public  int getOffset()
    {
        return (current-1)*limit;
    }
 
  
    /**
     * 获取总页数
     */
    public int getTotal()
    {
        if(rows%limit == 0)
        return rows/limit;
       else return rows/limit + 1;

    }

    /*
     获取起开始页  
    */
    public int getFrom()
    {
       int From = current - 2 ;
       return From < 1 ? 1 : From ;

    }
 

    /*
    获取结束页码
    */
  
    public int getTo()
    {
        int to = current+1;
        return to > getTotal() ? getTotal() : to ;
    }
    
       

  }