package com.extend.mybatis.plugin;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * ExamplePlugin.
 *
 * @author KevinClair
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class,  Object.class, RowBounds.class, ResultHandler.class})
})
public class ExamplePlugin implements Interceptor {

    private static final int MAPPED_STATEMENT_INDEX = 0;
    private static final int PARAMETER_INDEX = 1;
    private static final int ROW_BOUNDS_INDEX = 2;

    private static final Logger logger = LoggerFactory.getLogger(ExamplePlugin.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        RowBounds bounds = (RowBounds) args[ROW_BOUNDS_INDEX];
        if (bounds == RowBounds.DEFAULT){
            return invocation.proceed();
        }
        MappedStatement mappedStatement = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
        BoundSql boundSql = mappedStatement.getBoundSql(args[PARAMETER_INDEX]);
        String sql = boundSql.getSql() + String.format(" Limit %d,%d", bounds.getOffset(), bounds.getLimit());
//        bounds = RowBounds.DEFAULT;
        Field field = mappedStatement.getClass().getDeclaredField("sqlSource");
        field.setAccessible(true);
        field.set(mappedStatement, new StaticSqlSource(mappedStatement.getConfiguration(), sql, boundSql.getParameterMappings()));
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
