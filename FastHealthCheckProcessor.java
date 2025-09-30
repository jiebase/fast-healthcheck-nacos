import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.naming.healthcheck.AbstractHealthCheckProcessor;
import com.alibaba.nacos.naming.healthcheck.HealthCheckTask;
import com.alibaba.nacos.naming.healthcheck.HealthCheckType;

import java.net.Socket;
import java.util.Optional;

/**
 * FastHealthCheckProcessor 插件
 * 作者：Yuer（独立AI架构师）
 * 特点：
 * - 心跳检测更灵敏（10秒）
 * - 增加真实 TCP 探测
 * - 状态同步立即更新
 */
public class FastHealthCheckProcessor extends AbstractHealthCheckProcessor {

    @Override
    public String getType() {
        return "FAST_TCP";
    }

    @Override
    public void process(HealthCheckTask task) {
        Optional<Instance> optional = task.getCluster().getService().allIPs().stream()
            .filter(inst -> inst.getIp().equals(task.getIp()) && inst.getPort() == task.getPort())
            .findFirst();

        if (!optional.isPresent()) return;

        Instance instance = optional.get();
        long lastBeat = instance.getLastBeat();
        long now = System.currentTimeMillis();

        boolean timeout = (now - lastBeat) > 10000;
        boolean tcpAlive = isPortAlive(instance.getIp(), instance.getPort());

        if (timeout && !tcpAlive) {
            instance.setHealthy(false);
            task.getCluster().getService().updateInstance(instance);
            System.out.println("[FAST_TCP] DOWN: " + instance.getIp() + ":" + instance.getPort());
        } else {
            instance.setHealthy(true);
        }
    }

    private boolean isPortAlive(String ip, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(ip, port), 2000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public HealthCheckType getHealthCheckType() {
        return HealthCheckType.CUSTOM;
    }
}
