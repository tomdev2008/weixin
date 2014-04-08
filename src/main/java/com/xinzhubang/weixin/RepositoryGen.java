package com.xinzhubang.weixin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.b3log.latke.Latkes;
import org.b3log.latke.repository.jdbc.util.JdbcRepositories;

/**
 * 从数据库生成 repository.json.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Apr 8, 2014
 * @since 1.0.0
 */
public class RepositoryGen {

    public static void main(final String[] args) {
        Latkes.initRuntimeEnv();

        final Set<String> tableNames = new HashSet<String>(Arrays.asList(
                "T_Answer", "T_BProvince", "T_MGBook", "T_OItemsInfo", "T_User_Login_Log",
                "T_Major", "T_MNotice", "T_Question", "T_School", "T_User_Property", "T_UserCollection",
                "T_UserInfo", "T_Users", "T_Whisper"));

        JdbcRepositories.initRepositoryJSON(tableNames, "C:/repository.json");
    }
}
