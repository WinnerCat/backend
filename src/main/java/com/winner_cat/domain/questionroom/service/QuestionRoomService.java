package com.winner_cat.domain.questionroom.service;

import com.winner_cat.domain.questionroom.dto.AnswerDto;
import com.winner_cat.domain.questionroom.dto.CheckChattingRoomDetailDto;
import com.winner_cat.domain.questionroom.dto.QuestionDto;
import com.winner_cat.domain.questionroom.entity.Answer;
import com.winner_cat.domain.questionroom.entity.Question;
import com.winner_cat.domain.questionroom.entity.QuestionRoom;
import com.winner_cat.domain.questionroom.entity.enums.QuestionState;
import com.winner_cat.domain.questionroom.repository.AnswerRepository;
import com.winner_cat.domain.questionroom.repository.QuestionRepository;
import com.winner_cat.domain.questionroom.repository.QuestionRoomRepository;
import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.exception.GeneralException;
import com.winner_cat.global.response.ApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionRoomService {
    private final QuestionRoomRepository questionRoomRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    /**
     * 질문 방 상태 수정
     */
    public ResponseEntity<?> changeState(Long questionRoomId,QuestionState state) {
        // 1. 질문방 존재하는지 확인
        QuestionRoom questionRoom = questionRoomRepository.findById(questionRoomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_ROOM_NOT_FOUND));

        // 2. 상태 변경
        questionRoom.changeState(state);

        return ResponseEntity.ok().body(ApiResponse.onSuccess("상태 변경에 성공하였습니다."));
    }

    /**
     * 특정 질문 방 대화 목록 상세 보기
     */
    public ResponseEntity<?> getQuestionRoomDetail(Long questionRoomId) {
        // 1. 해당하는 채팅방이 존재하는지 확인
        QuestionRoom questionRoom = questionRoomRepository.findById(questionRoomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_ROOM_NOT_FOUND));
        // 2. 질문방으로부터 질문 리스트 얻어오기
        List<Question> questionList = questionRepository.findByQuestionRoomOrderByCreatedAt(questionRoom);
        // 3. 질문방으로부터 답변 리스트 얻어오기
        List<Answer> answerList = answerRepository.findByQuestionRoomOrderByCreatedAt(questionRoom);
        // 4. DTO 생성 후 반환
        ArrayList<QuestionDto> questionDtoArrayList = new ArrayList<>();
        for (Question question : questionList) {
            QuestionDto questionDto = QuestionDto.builder()
                    .content(question.getContent())
                    .createdAt(question.getCreatedAt())
                    .build();
            questionDtoArrayList.add(questionDto);
        }
        ArrayList<AnswerDto> answerDtoArrayList = new ArrayList<>();
        for (Answer answer : answerList) {
            AnswerDto answerDto = AnswerDto.builder()
                    .content(answer.getContent())
                    .createdAt(answer.getCreatedAt())
                    .build();
            answerDtoArrayList.add(answerDto);
        }
        CheckChattingRoomDetailDto result = CheckChattingRoomDetailDto.builder()
                .questionList(questionDtoArrayList)
                .answerList(answerDtoArrayList)
                .build();

        return ResponseEntity.ok().body(ApiResponse.onSuccess(result));
    }
}
