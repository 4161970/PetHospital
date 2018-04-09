package ph.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ph.entity.Speciality;
import ph.entity.Vet;

public class VetDAO {
    public void save(Vet vet) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ph",
                    "root", "4161970");
            con.setAutoCommit(false);
            String sql = "insert into t_vet value(null,?)";
            ps = con.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            //PreparedStatement.RETURN_GENERATED_KEYS返回自动生成的键的标志
            ps.setString(1, vet.getName());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            //getGeneratedKeys()与PreparedStatement.RETURN_GENERATED_KEYS一起使用获取自增主键
            //获取自动生成的主键创建此Statement对象执行的结果。如果此Statement对象没有产生任何键，则返回空的ResultSet对象。
            if (rs.next()) {
                vet.setId(rs.getInt(1));
                /*现有表User：列有id,name.
                String sql="select * from User";
                ResultSet rs = null;
                rs = st.executeQuery(sql);
                while(rs.next){
                    rs.getInt(1)//等价于rs.getInt("id");
                    rs.getString(2)//等价于rs.getInt("name");
                }*/
            }
            sql = "insert into t_vet_speciality values";
            boolean first = true;
            for (Speciality spec : vet.getSpecs()) {
                if (first) {
                    sql += "(" + vet.getId() + "," + spec.getId() + ")";
                    first = false;
                } else {
                    sql += ",(" + vet.getId() + "," + spec.getId() + ")";
                }
            }
            ps.executeUpdate(sql);
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (con != null)
                con.rollback();
            throw new Exception("数据库访问出现异常:" + e);
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (con != null)
                con.close();
        }
    }

    public List<Vet> search(String vetName, String specName) throws Exception {
        List<Vet> vets = new ArrayList<Vet>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ph",
                    "root", "4161970");
            con.setAutoCommit(false);
            ps = con.prepareStatement("SELECT distinct  t_vet.* FROM    t_vet_speciality    "
                    + "INNER JOIN t_speciality ON (t_vet_speciality.specId = t_speciality.id)   "
                    + "INNER JOIN ph.t_vet  ON (t_vet_speciality.vetId = t_vet.id) "
                    + "where t_vet.name like  ? and t_speciality.name like ?");
            ps.setString(1, "%"+vetName+"%");
            ps.setString(2, "%"+specName+"%");
            rs=ps.executeQuery();
            while(rs.next()){
                Vet v=new Vet();
                v.setId(rs.getInt("id"));
                v.setName(rs.getString("name"));
                vets.add(v);
            }

            for(Vet v:vets){
                rs=ps.executeQuery("SELECT t_speciality.* FROM    t_vet_speciality    "
                        + "INNER JOIN t_speciality ON (t_vet_speciality.specId = t_speciality.id)   "
                        + "INNER JOIN ph.t_vet  ON (t_vet_speciality.vetId = t_vet.id) "
                        + "where t_vet.id =  "+v.getId());

                while(rs.next()){
                    Speciality spec=new Speciality();
                    spec.setId(rs.getInt("id"));
                    spec.setName(rs.getString("name"));
                    v.getSpecs().add(spec);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("数据库访问出现异常:" + e);
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (con != null)
                con.close();
        }
        return vets;
    }

    public List<Vet> getAll() throws Exception {
        List<Vet> vets = new ArrayList<Vet>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ph",
                    "root", "4161970");
            ps=con.prepareStatement("select * from t_vet ");
            rs=ps.executeQuery();
            while(rs.next()){
                Vet v=new Vet();
                v.setId(rs.getInt("id"));
                v.setName(rs.getString("name"));
                vets.add(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("数据库访问出现异常:" + e);
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (con != null)
                con.close();
        }
        return vets;
    }
}
