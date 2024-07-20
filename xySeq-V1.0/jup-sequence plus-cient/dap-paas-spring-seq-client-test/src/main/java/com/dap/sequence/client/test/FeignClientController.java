package com.dap.sequence.client.test;

import com.dap.sequence.api.core.SeqConsumer;
import com.dap.sequence.api.dto.SeqCallbackDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.util.DateUtils;
import com.dap.sequence.client.core.SequenceQueue;
import com.dap.sequence.client.core.SequenceQueueFactory;
import com.dap.sequence.client.service.SequenceGeneratorClient;
import com.dap.sequence.client.service.impl.SequenceGeneratorClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther zpj
 * @descriptions
 * @Date 2021-09-10 15:15
 */

@RestController
public class FeignClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceGeneratorClientImpl.class);

    private static ExecutorService executor1 = new ThreadPoolExecutor(500, 1000, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(50000));

    @Autowired
    private SequenceGeneratorClient sequenceGeneratorClient;

    @Autowired
    private SeqConsumer seqConsumer;


    /**
     * 雪花算法获取全局唯一id
     *
     * @return String
     */
    @RequestMapping(value = "getSnowFlake", method = RequestMethod.GET)
    public String getSnowFlake() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String seq = sequenceGeneratorClient.getSnowFlake();
        stopWatch.stop();
        LOGGER.warn("获取雪花序号【{}】时间消耗：{}", seq, stopWatch.getTotalTimeMillis());
        return seq;
    }

    /**
     * 序列随sign变化重新开始
     *
     * @param seqCode seqCode
     * @return String
     */
    @RequestMapping(value = "getSequenceCode/{seqCode}", method = RequestMethod.GET)
    public ResultResponse getStringSequence(@PathVariable("seqCode") String seqCode, @RequestParam(required = false) String sign) {

        ResultResponse resultResponse;
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            String[] place = new String[]{"20240710","19"};
            String seq = sequenceGeneratorClient.getStringSequence(seqCode, sign, place);
            stopWatch.stop();
            LOGGER.warn("获取序号【{}】时间消耗：{}", seq, stopWatch.getTotalTimeMillis());
            resultResponse = ResultResponse.success(seq);
        } catch (SequenceException e) {
            resultResponse = ResultResponse.error(e.getErrorCode(), e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 获取普通非自选序列
     *
     * @param seqCode seqCode
     * @return ResultResponse
     */
    @RequestMapping(value = "getStringSequence/{seqCode}", method = RequestMethod.GET)
    public ResultResponse getStringSequence(@PathVariable("seqCode") String seqCode) {
        ResultResponse resultResponse;
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            String sequence = sequenceGeneratorClient.getStringSequence(seqCode);
            stopWatch.stop();
            LOGGER.warn("获取序号【{}】时间消耗：{}", sequence, stopWatch.getTotalTimeMillis());
            resultResponse = ResultResponse.success(sequence);
        } catch (SequenceException e) {
            resultResponse = ResultResponse.error(e.getErrorCode(), e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 获取普通非自选序列
     *
     * @param seqCode seqCode
     * @return ResultResponse
     */
    @RequestMapping(value = "getIncreaseSequence/{seqCode}", method = RequestMethod.GET)
    public ResultResponse getIncreaseSequence(@PathVariable("seqCode") String seqCode) {
        //Random random = new Random();
        //for (int i = 0; i < 100; i++) {
        //    executor1.execute(() -> {
        //        try {
        //            Thread.sleep(random.nextInt(1000) + 1);
        //        } catch (InterruptedException e) {
        //            throw new RuntimeException(e);
        //        }
        //        try {
        //            String[] place = new String[]{"QQQ", "WWWWW"};
        //            StopWatch stopWatch = new StopWatch();
        //            stopWatch.start();
        //            String sequence = sequenceGeneratorClient.getIncreaseSequence(seqCode, place);
        //            stopWatch.stop();
        //            LOGGER.warn("获取严格递增序号【{}】时间消耗：{}", sequence, stopWatch.getTotalTimeMillis());
        //        } catch (SequenceException e) {
        //            LOGGER.error("获取序列报错:{}", e.getMessage());
        //        }
        //    });
        //}
        //return null;
        ResultResponse resultResponse;
        try {
            String[] place = new String[]{"QQQ", "WWWWW"};
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            List<String> sequence = sequenceGeneratorClient.getIncreaseSequence(seqCode, 10,place);
            stopWatch.stop();
            LOGGER.warn("获取严格递增序号【{}】时间消耗：{}", sequence, stopWatch.getTotalTimeMillis());
            resultResponse = ResultResponse.success(sequence);
        } catch (SequenceException e) {
            resultResponse = ResultResponse.error(e.getErrorCode(), e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 获取普通非自选序列
     *
     * @param seqCode seqCode
     * @return ResultResponse
     */
    @RequestMapping(value = "getRecoverySequence/{seqCode}", method = RequestMethod.GET)
    public ResultResponse getRecoverySequence(@PathVariable("seqCode") String seqCode) {
        ResultResponse resultResponse;
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            String sequence = sequenceGeneratorClient.getRecoverySequence(seqCode);
            stopWatch.stop();
            LOGGER.warn("获取回收序号【{}】时间消耗：{}", sequence, stopWatch.getTotalTimeMillis());
            resultResponse = ResultResponse.success(sequence);
        } catch (SequenceException e) {
            resultResponse = ResultResponse.error(e.getErrorCode(), e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 获取普通非自选序列
     *
     * @param seqCode seqCode
     * @return ResultResponse
     */
    @RequestMapping(value = "recoverySequence/{seqCode}", method = RequestMethod.POST)
    public ResultResponse recoverySequence(@PathVariable("seqCode") String seqCode, @RequestBody Map<String, String> params) {
        ResultResponse resultResponse;
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            String serialNumber = params.get("serialNumber");
            boolean sequence = sequenceGeneratorClient.recoverySequence(seqCode, serialNumber);
            stopWatch.stop();
            LOGGER.warn("回收序号【{}】时间消耗：{}", serialNumber, stopWatch.getTotalTimeMillis());
            resultResponse = ResultResponse.success(sequence);
        } catch (SequenceException e) {
            resultResponse = ResultResponse.error(e.getErrorCode(), e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 获取自选序列
     *
     * @param seqCode seqCode
     * @param seqVal  seqVal
     * @return ResultResponse
     */
    @RequestMapping(value = "getOptionalSequence/{seqCode}/{seqVal}", method = RequestMethod.GET)
    public ResultResponse getOptionalSequence(@PathVariable("seqCode") String seqCode, @PathVariable("seqVal") String seqVal) {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
                int num = random.nextInt(30) + 1;
                try {
                    Thread.sleep(num);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ResultResponse resultResponse = null;
                try {
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    //SeqTransferDto sequence = sequenceGeneratorClient.getOptionalSequence(seqCode, String.valueOf(random.nextInt(10000) + 1), 200);
                    SeqTransferDto sequence = sequenceGeneratorClient.getOptionalSequence(seqCode, seqVal, 10);
                    stopWatch.stop();
                    LOGGER.warn("获取自选序号【{}】时间消耗：{}", seqCode, stopWatch.getTotalTimeMillis());
                    resultResponse = ResultResponse.success(sequence);
                } catch (SequenceException e) {
                    LOGGER.error("获取序列报错:{}", e.getMessage());
                }
                ResultResponse resultResponse1 = resultResponse;
                return resultResponse1;
        }
        return null;
        //ResultResponse resultResponse;
        //try {
        //    StopWatch stopWatch = new StopWatch();
        //    stopWatch.start();
        //    SeqTransferDto sequence = sequenceGeneratorClient.getOptionalSequence(seqCode, String.valueOf(random.nextInt(10000) + 1), 200);
        //    //SeqTransferDto sequence = sequenceGeneratorClient.getOptionalSequence(seqCode, seqVal, 200);
        //    stopWatch.stop();
        //    LOGGER.warn("获取自选序号【{}】时间消耗：{}", seqCode, stopWatch.getTotalTimeMillis());
        //    resultResponse = ResultResponse.success(sequence);
        //} catch (SequenceException e) {
        //    resultResponse = ResultResponse.error(e.getErrorCode(), e.getMessage());
        //}
        //return resultResponse;
    }

    /**
     * 选中自选序号
     *
     * @param params params
     * @return ResultResponse
     */
    @RequestMapping(value = "selectedOptionalSequence", method = RequestMethod.POST)
    public ResultResponse selectedOptionalSequence(@RequestBody Map<String, String> params) {
        ResultResponse resultResponse;
        try {
            String seqCode = params.get("seqCode");
            String seqNo = params.get("seqNo");
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            SeqTransferDto sequence = sequenceGeneratorClient.selectedOptionalSequence(seqCode, seqNo);
            stopWatch.stop();
            LOGGER.warn("选中自选序号【{}】时间消耗：{}", seqNo, stopWatch.getTotalTimeMillis());
            resultResponse = ResultResponse.success(sequence);
        } catch (SequenceException e) {
            resultResponse = ResultResponse.error(e.getErrorCode(), e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 回收自选序号
     *
     * @param params params
     * @return ResultResponse
     */
    @RequestMapping(value = "cancelOptionalSequence", method = RequestMethod.POST)
    public ResultResponse cancelOptionalSequence(@RequestBody Map<String, String> params) {
        ResultResponse resultResponse;
        try {
            String seqCode = params.get("seqCode");
            String seqNo = params.get("seqNo");
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            SeqTransferDto sequence = sequenceGeneratorClient.cancelOptionalSequence(seqCode, seqNo);
            stopWatch.stop();
            LOGGER.warn("取消自选序号【{}】时间消耗：{}", seqNo, stopWatch.getTotalTimeMillis());
            resultResponse = ResultResponse.success(sequence);
        } catch (SequenceException e) {
            resultResponse = ResultResponse.error(e.getErrorCode(), e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 获取日切序列 系统时间
     *
     * @param seqCode seqCode
     * @return ResultResponse
     */
    @RequestMapping(value = "getDaySwitchSequence/{seqCode}", method = RequestMethod.GET)
    public ResultResponse getDaySwitchSequence(@PathVariable("seqCode") String seqCode) {
        //Random random = new Random();
        //for (int i = 0; i < 10000; i++) {
        //    executor1.execute(() -> {
        //        try {
        //            Thread.sleep(random.nextInt(1000) + 1);
        //        } catch (InterruptedException e) {
        //            throw new RuntimeException(e);
        //        }
        //        try {
        //            StopWatch stopWatch = new StopWatch();
        //            stopWatch.start();
        //            String seq = sequenceGeneratorClient.getDaySwitchSequence(seqCode, new Date());
        //            stopWatch.stop();
        //            LOGGER.warn("获取序号【{}】时间消耗：{}", seq, stopWatch.getTotalTimeMillis());
        //        } catch (SequenceException e) {
        //            LOGGER.error("获取序列报错:{}", e.getMessage());
        //        }
        //    });
        //}
        //return null;
        ResultResponse resultResponse;
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            String sequence = sequenceGeneratorClient.getCycleSequence(seqCode, LocalDate.now());
            stopWatch.stop();
            LOGGER.warn("取消回拨序号【{}】时间消耗：{}", sequence, stopWatch.getTotalTimeMillis());
            resultResponse = ResultResponse.success(sequence);
        } catch (SequenceException e) {
            resultResponse = ResultResponse.error(e.getErrorCode(), e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 获取日切序列 工作时间
     *
     * @param seqCode seqCode
     * @return ResultResponse
     */
    @RequestMapping(value = "getDaySwitchSequenceDate/{seqCode}", method = RequestMethod.GET)
    public ResultResponse getDaySwitchSequenceDate(@PathVariable("seqCode") String seqCode, @RequestParam String date) {
        ResultResponse resultResponse;
        try {
            Date date1 = DateUtils.converToDate(date, DateUtils.DATE_FORMAT);
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            String sequence = sequenceGeneratorClient.getCycleSequence(seqCode, LocalDate.of(date1.getYear(),date1.getMonth(),date1.getDay()));
            stopWatch.stop();
            LOGGER.warn("取消回拨序号【{}】时间消耗：{}", sequence, stopWatch.getTotalTimeMillis());
            resultResponse = ResultResponse.success(sequence);
        } catch (SequenceException e) {
            resultResponse = ResultResponse.error(e.getErrorCode(), e.getMessage());
        }
        return resultResponse;

    }

    @RequestMapping(value = "callback", method = RequestMethod.GET)
    public String callback() {
        ConcurrentHashMap<String, SequenceQueue> SEQUENCE_QUEUE_MAP = SequenceQueueFactory.SEQUENCE_QUEUE_MAP;
        for (String seqCode : SEQUENCE_QUEUE_MAP.keySet()) {
            SequenceQueue sequenceQueue = SEQUENCE_QUEUE_MAP.get(seqCode);
            if (sequenceQueue != null && sequenceQueue.getCacheBlockingQueue() != null && !sequenceQueue.getCacheBlockingQueue().isEmpty()) {
                SeqCallbackDto seqCallbackDto = new SeqCallbackDto();
                seqCallbackDto.setSequenceCode(seqCode);
                List<Object> callbackNumQueue = new ArrayList<>(sequenceQueue.getCacheBlockingQueue());
                seqCallbackDto.setCallbackNumQueue(callbackNumQueue);
                seqConsumer.clientCacheQueueCallBack(seqCallbackDto);
            }
        }
        return "";
    }


    @PostMapping(value = "getSequenceStr")
    public ResultResponse getSequenceStr(@RequestBody DemoParam demoParam) {
        ResultResponse resultResponse = null;
        try {
            String seqCode = demoParam.getSeqCode();
//            List<String> placeList = demoParam.getPlaceList();
//            String[] place = new String[0];
//            if (!CollectionUtils.isEmpty(placeList)) {
//                place = placeList.toArray(new String[0]);
//            }
            String[] place = new String[]{"19"};
            String stringSequence = sequenceGeneratorClient.getStringSequence(seqCode, place);
            resultResponse = ResultResponse.success(stringSequence);
        } catch (SequenceException e) {
            resultResponse = ResultResponse.error(e.getErrorCode(), e.getMessage());
        } catch (Throwable e) {

            throw e;
        }
        return resultResponse;
    }



    /**
     * 获取自选序列
     *
     * @param seqCode seqCode
     * @param seqVal  seqVal
     * @return ResultResponse
     */
    @RequestMapping(value = "getOptSeq/{seqCode}/{seqVal}/{reqNum}", method = RequestMethod.GET)
    public ResultResponse getOptSeq(@PathVariable("seqCode") String seqCode, @PathVariable("seqVal") String seqVal,@PathVariable("reqNum") String reqNum) {
        SeqTransferDto sequence = sequenceGeneratorClient.getOptionalSequence(seqCode, seqVal, Integer.valueOf(reqNum));
        return ResultResponse.success(sequence);
    }
}
