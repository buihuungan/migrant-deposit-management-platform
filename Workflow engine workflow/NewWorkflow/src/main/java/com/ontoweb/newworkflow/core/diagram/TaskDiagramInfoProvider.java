package com.ontoweb.newworkflow.core.diagram;

import org.springframework.util.StopWatch;

import java.util.List;

/**
 * @author ：Ethan yan
 * @date ：Created in 2021/8/1 下午10:19
 * @description：
 * @modified By：
 * @version: $
 */
public interface TaskDiagramInfoProvider {
    boolean disable();
    String getInfo(String nodeName, List<StopWatch.TaskInfo> tasks);
}
