package cn.sfpy.sfpy;

import cn.sfpy.pojo.TB_SFPY_USER;
import cn.sfpy.session.JaveSession;

public class tester {
	public static void main(String args[]) throws Exception {
        //���NetJavaSession����
		JaveSession session = new JaveSession();
        //����һ��UserInfo����
        TB_SFPY_USER user = new TB_SFPY_USER();
        //���ö��������
        user.setUser_name("zhangkai");
        user.setUser_phone("15996211994");
        user.setUser_pwd("1234");
        //user.setUser_id(1);
        //�����󱣴浽���ݿ���
        String sql = session.getSaveObjectSql(user);
        System.out.println("��������sql��䣺" + sql);
        
        session.saveObject(user);
        System.out.println("����ɹ���");
       /* //���Ҷ���
        TB_SFPY_USER userInfo = (TB_SFPY_USER) session.getObject( "cn.sfpy.pojo.TB_SFPY_USER", 6988);
        System.out.println("��ȡ������Ϣ��" + userInfo);*/
 
    }
}
