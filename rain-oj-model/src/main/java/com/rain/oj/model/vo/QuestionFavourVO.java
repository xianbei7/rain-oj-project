package com.rain.oj.model.vo;

import cn.hutool.json.JSONUtil;
import com.rain.oj.model.bo.QuestionFavourBO;
import com.rain.oj.model.enums.QuestionDifficultyEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目收藏视图
 */
@Data
public class QuestionFavourVO implements Serializable {

    /**
     * id
     */
    private Long questionId;

    /**
     * 编号
     */
    private Integer number;

    /**
     * 标题
     */
    private String title;

    /**
     * 难度
     */
    private String difficulty;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建题目人的信息
     */
    private UserVO userVO;

    /**
     * 是否已收藏
     */
    private Boolean hasFavour;

    /**
     * 收藏时间
     */
    private Date favourTime;

    private static final long serialVersionUID = 1L;

   /* *
     * 包装类转对象
     *
     * @param questionVO
     * @return

    public static Question voToObj(QuestionFavourVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        String difficulty = questionVO.getDifficulty();
        question.setDifficulty(QuestionDifficultyEnum.getEnumByText(difficulty).getValue());
        List<String> tagList = questionVO.getTags();
        if (tagList != null) {
            question.setTags(JSONUtil.toJsonStr(tagList));
        }
        return question;
    }*/

    /**
     * 对象转包装类
     *
     * @param questionFavourBO 题目收藏bo
     * @return {@link QuestionFavourVO} 题目收藏vo
     */
    public static QuestionFavourVO boToVo(QuestionFavourBO questionFavourBO) {
        if (questionFavourBO == null) {
            return null;
        }
        QuestionFavourVO questionFavourVO = new QuestionFavourVO();
        BeanUtils.copyProperties(questionFavourBO, questionFavourVO);
        Integer difficulty = questionFavourBO.getDifficulty();
        questionFavourVO.setDifficulty(QuestionDifficultyEnum.getEnumByValue(difficulty).getDifficulty());
        List<String> tagList = JSONUtil.toList(questionFavourBO.getTags(), String.class);
        questionFavourVO.setTags(tagList);
        questionFavourVO.setFavourTime(questionFavourBO.getCreateTime());
        return questionFavourVO;
    }
}