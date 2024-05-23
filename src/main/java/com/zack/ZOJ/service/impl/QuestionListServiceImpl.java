package com.zack.ZOJ.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zack.ZOJ.common.ErrorCode;
import com.zack.ZOJ.constant.CommonConstant;
import com.zack.ZOJ.exception.BusinessException;
import com.zack.ZOJ.exception.ThrowUtils;
import com.zack.ZOJ.model.dto.questionlist.QuestionCase;
import com.zack.ZOJ.model.dto.questionlist.QuestionListQueryRequest;
import com.zack.ZOJ.model.entity.Question;
import com.zack.ZOJ.model.entity.QuestionList;
import com.zack.ZOJ.model.entity.User;
import com.zack.ZOJ.model.vo.QuestionListVO;
import com.zack.ZOJ.model.vo.QuestionVO;
import com.zack.ZOJ.model.vo.UserVO;
import com.zack.ZOJ.service.QuestionListService;
import com.zack.ZOJ.mapper.QuestionListMapper;
import com.zack.ZOJ.service.UserService;
import com.zack.ZOJ.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zls5600
 * @description 针对表【question_list(题单)】的数据库操作Service实现
 * @createDate 2024-05-23 13:03:52
 */
@Service
public class QuestionListServiceImpl extends ServiceImpl<QuestionListMapper, QuestionList>
        implements QuestionListService {


    @Resource
    private UserService userService;

//    @Resource
//    private QuestionThumbMapper questionThumbMapper;
//
//    @Resource
//    private QuestionFavourMapper questionFavourMapper;

//    @Resource
//    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 校验题目是否合格
     *
     * @param questionList
     * @param add
     */
    @Override
    public void validQuestionList(QuestionList questionList, boolean add) {
        if (questionList == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = questionList.getTitle();
        String content = questionList.getContent();
        String tags = questionList.getTags();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }

    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param questionListQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionList> getQueryWrapper(QuestionListQueryRequest questionListQueryRequest) {
        QueryWrapper<QuestionList> queryWrapper = new QueryWrapper<>();
        if (questionListQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionListQueryRequest.getId();
        String title = questionListQueryRequest.getTitle();
        String content = questionListQueryRequest.getContent();
        List<String> tags = questionListQueryRequest.getTags();
        Long userId = questionListQueryRequest.getUserId();
        String sortField = questionListQueryRequest.getSortField();
        String sortOrder = questionListQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);

        return queryWrapper;
    }

//    @Override
//    public Page<Question> searchFromEs(QuestionQueryRequest questionQueryRequest) {
//        Long id = questionQueryRequest.getId();
//        Long notId = questionQueryRequest.getNotId();
//        String searchText = questionQueryRequest.getSearchText();
//        String title = questionQueryRequest.getTitle();
//        String content = questionQueryRequest.getContent();
//        List<String> tagList = questionQueryRequest.getTags();
//        List<String> orTagList = questionQueryRequest.getOrTags();
//        Long userId = questionQueryRequest.getUserId();
//        // es 起始页为 0
//        long current = questionQueryRequest.getCurrent() - 1;
//        long pageSize = questionQueryRequest.getPageSize();
//        String sortField = questionQueryRequest.getSortField();
//        String sortOrder = questionQueryRequest.getSortOrder();
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        // 过滤
//        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete", 0));
//        if (id != null) {
//            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
//        }
//        if (notId != null) {
//            boolQueryBuilder.mustNot(QueryBuilders.termQuery("id", notId));
//        }
//        if (userId != null) {
//            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", userId));
//        }
//        // 必须包含所有标签
//        if (CollUtil.isNotEmpty(tagList)) {
//            for (String tag : tagList) {
//                boolQueryBuilder.filter(QueryBuilders.termQuery("tags", tag));
//            }
//        }
//        // 包含任何一个标签即可
//        if (CollUtil.isNotEmpty(orTagList)) {
//            BoolQueryBuilder orTagBoolQueryBuilder = QueryBuilders.boolQuery();
//            for (String tag : orTagList) {
//                orTagBoolQueryBuilder.should(QueryBuilders.termQuery("tags", tag));
//            }
//            orTagBoolQueryBuilder.minimumShouldMatch(1);
//            boolQueryBuilder.filter(orTagBoolQueryBuilder);
//        }
//        // 按关键词检索
//        if (StringUtils.isNotBlank(searchText)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("title", searchText));
//            boolQueryBuilder.should(QueryBuilders.matchQuery("description", searchText));
//            boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchText));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
//        // 按标题检索
//        if (StringUtils.isNotBlank(title)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("title", title));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
//        // 按内容检索
//        if (StringUtils.isNotBlank(content)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("content", content));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
//        // 排序
//        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
//        if (StringUtils.isNotBlank(sortField)) {
//            sortBuilder = SortBuilders.fieldSort(sortField);
//            sortBuilder.order(CommonConstant.SORT_ORDER_ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
//        }
//        // 分页
//        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
//        // 构造查询
//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
//                .withPageable(pageRequest).withSorts(sortBuilder).build();
//        SearchHits<QuestionEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, QuestionEsDTO.class);
//        Page<Question> page = new Page<>();
//        page.setTotal(searchHits.getTotalHits());
//        List<Question> resourceList = new ArrayList<>();
//        // 查出结果后，从 db 获取最新动态数据（比如点赞数）
//        if (searchHits.hasSearchHits()) {
//            List<SearchHit<QuestionEsDTO>> searchHitList = searchHits.getSearchHits();
//            List<Long> questionIdList = searchHitList.stream().map(searchHit -> searchHit.getContent().getId())
//                    .collect(Collectors.toList());
//            List<Question> questionList = baseMapper.selectBatchIds(questionIdList);
//            if (questionList != null) {
//                Map<Long, List<Question>> idQuestionMap = questionList.stream().collect(Collectors.groupingBy(Question::getId));
//                questionIdList.forEach(questionId -> {
//                    if (idQuestionMap.containsKey(questionId)) {
//                        resourceList.add(idQuestionMap.get(questionId).get(0));
//                    } else {
//                        // 从 es 清空 db 已物理删除的数据
//                        String delete = elasticsearchRestTemplate.delete(String.valueOf(questionId), QuestionEsDTO.class);
//                        log.info("delete question {}", delete);
//                    }
//                });
//            }
//        }
//        page.setRecords(resourceList);
//        return page;
//    }

    @Override
    public QuestionListVO getQuestionListVO(QuestionList questionList, HttpServletRequest request) {
        if (questionList == null) {
            return null;
        }
        QuestionListVO questionListVO = new QuestionListVO();
        BeanUtils.copyProperties(questionList, questionListVO);
        List<String> tagList = JSONUtil.toList(questionList.getTags(), String.class);
        questionListVO.setTags(tagList);
        List<Question> questionCase = JSONUtil.toList(questionList.getQuestionCase(), Question.class);
        questionListVO.setQuestionCase(questionCase);

        // 关联查询用户信息
        Long userId = questionList.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionListVO.setUserVO(userVO);
        // todo 实现点赞收藏等
//        // 2. 已登录，获取用户点赞、收藏状态
//        User loginUser = userService.getLoginUserPermitNull(request);
//        if (loginUser != null) {
//            // 获取点赞
//            QueryWrapper<QuestionThumb> questionThumbQueryWrapper = new QueryWrapper<>();
//            questionThumbQueryWrapper.in("questionId", questionId);
//            questionThumbQueryWrapper.eq("userId", loginUser.getId());
//            QuestionThumb questionThumb = questionThumbMapper.selectOne(questionThumbQueryWrapper);
//            questionVO.setHasThumb(questionThumb != null);
//            // 获取收藏
//            QueryWrapper<QuestionFavour> questionFavourQueryWrapper = new QueryWrapper<>();
//            questionFavourQueryWrapper.in("questionId", questionId);
//            questionFavourQueryWrapper.eq("userId", loginUser.getId());
//            QuestionFavour questionFavour = questionFavourMapper.selectOne(questionFavourQueryWrapper);
//            questionVO.setHasFavour(questionFavour != null);
//        }
        return questionListVO;
    }

    @Override
    public Page<QuestionListVO> getQuestionListVOPage(Page<QuestionList> questionListPage, HttpServletRequest request) {
        List<QuestionList> questionLists = questionListPage.getRecords();
        Page<QuestionListVO> questionListVOPage = new Page<>(questionListPage.getCurrent(), questionListPage.getSize(), questionListPage.getTotal());
        if (CollUtil.isEmpty(questionLists)) {
            return questionListVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionLists.stream().map(QuestionList::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
//        // 2. 已登录，获取用户点赞、收藏状态
//        Map<Long, Boolean> questionIdHasThumbMap = new HashMap<>();
//        Map<Long, Boolean> questionIdHasFavourMap = new HashMap<>();
//        User loginUser = userService.getLoginUserPermitNull(request);
//        if (loginUser != null) {
//            Set<Long> questionIdSet = questionList.stream().map(Question::getId).collect(Collectors.toSet());
//            loginUser = userService.getLoginUser(request);
//            // 获取点赞
//            QueryWrapper<QuestionThumb> questionThumbQueryWrapper = new QueryWrapper<>();
//            questionThumbQueryWrapper.in("questionId", questionIdSet);
//            questionThumbQueryWrapper.eq("userId", loginUser.getId());
//            List<QuestionThumb> questionQuestionThumbList = questionThumbMapper.selectList(questionThumbQueryWrapper);
//            questionQuestionThumbList.forEach(questionQuestionThumb -> questionIdHasThumbMap.put(questionQuestionThumb.getQuestionId(), true));
//            // 获取收藏
//            QueryWrapper<QuestionFavour> questionFavourQueryWrapper = new QueryWrapper<>();
//            questionFavourQueryWrapper.in("questionId", questionIdSet);
//            questionFavourQueryWrapper.eq("userId", loginUser.getId());
//            List<QuestionFavour> questionFavourList = questionFavourMapper.selectList(questionFavourQueryWrapper);
//            questionFavourList.forEach(questionFavour -> questionIdHasFavourMap.put(questionFavour.getQuestionId(), true));
//        }
        // 填充信息
        List<QuestionListVO> questionVOList = questionLists.stream().map(questionList -> {
            QuestionListVO questionListVO = getQuestionListVO(questionList, request);
            Long userId = questionList.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionListVO.setUserVO(userService.getUserVO(user));
//            questionVO.setHasThumb(questionIdHasThumbMap.getOrDefault(question.getId(), false));
//            questionVO.setHasFavour(questionIdHasFavourMap.getOrDefault(question.getId(), false));
            return questionListVO;
        }).collect(Collectors.toList());
        questionListVOPage.setRecords(questionVOList);
        return questionListVOPage;
    }
}




