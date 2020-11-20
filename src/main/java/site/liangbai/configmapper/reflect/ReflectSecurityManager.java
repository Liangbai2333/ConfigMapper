package site.liangbai.configmapper.reflect;

public class ReflectSecurityManager extends SecurityManager {
    @Override
    public Class<?>[] getClassContext() {
        return super.getClassContext();
    }
}
