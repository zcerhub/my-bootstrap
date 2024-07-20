package com.dap.sequence.client.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.dap.sequence.api.exception.ResultResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dap.sequence.client.core.SequenceQueueFactory;

/**
 * @className DapSeqApiResourceController
 * @description 资源控制类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@Controller
@RequestMapping("/seq-client")
public class DapSeqApiResourceController {

    @RequestMapping(value = "/getSeq/buffer")
    @ResponseBody
    public ResultResponse uiConfiguration() {
        List<Map<String, Object>> seqBufferMapList = new ArrayList<>();
        SequenceQueueFactory.SEQUENCE_QUEUE_MAP.forEach((k, v) -> {
            Map<String, Object> seqBufferMap = new HashMap<>();
            seqBufferMap.put("seqCode", k);
            seqBufferMap.put("lastGetTime", v.getLastGetTime());
            List<Map<String, Object>> queueList = new ArrayList<>();
            BlockingQueue<Object> blockingQueue = v.getBufferQueue();

            int seqTotal = 0;
            if (blockingQueue != null) {
                Map<String, Object> map = getSeqUsed(blockingQueue, 1);
                seqTotal += (Integer) map.get("size");
                queueList.add(getSeqUsed(blockingQueue, 1));
            }
            BlockingQueue<BlockingQueue<Object>> quenList = v.getQuenList();
            if (!CollectionUtils.isEmpty(quenList)) {
                for (BlockingQueue<Object> b : quenList) {
                    Map<String, Object> map = getSeqUsed(b, 0);
                    seqTotal += (Integer) map.get("size");
                    queueList.add(getSeqUsed(b, 0));
                }
            }
            seqBufferMap.put("seqTotal", seqTotal);
            seqBufferMap.put("queueList", queueList);
            seqBufferMapList.add(seqBufferMap);
        });
        return ResultResponse.success(seqBufferMapList);
    }

    public Map<String, Object> getSeqUsed(BlockingQueue<Object> blockingQueue, int used) {
        Object[] ogjs = blockingQueue.toArray();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("used", used);
        if (ogjs.length > 0) {
            map.put("startSeq", ogjs[0]);
            map.put("endSeq", ogjs[ogjs.length - 1]);
            map.put("size", ogjs.length);
        }
        return map;
    }
}