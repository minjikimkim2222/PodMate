package com.podmate.domain.jjim.application;

import com.podmate.domain.jjim.domain.entity.JJim;
import com.podmate.domain.jjim.domain.repository.JJimRepository;
import com.podmate.domain.jjim.dto.JJimResponseDto;
import com.podmate.domain.jjim.exception.DuplicateJJimException;
import com.podmate.domain.jjim.exception.JJimNotFoundException;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.repository.PodRepository;
import com.podmate.domain.pod.exception.PodNotFoundException;
import com.podmate.domain.podUserMapping.domain.repository.PodUserMappingRepository;
import com.podmate.domain.podUserMapping.exception.PodUserMappingNotFoundException;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class JJimService {

    private final UserRepository userRepository;
    private final PodRepository podRepository;
    private final PodUserMappingRepository podUserMappingRepository;
    private final JJimRepository jjimRepository;

    public void createJJim(Long podId, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());

//        if(!podUserMappingRepository.existsByPod_IdAndUser_Id(pod.getId(), user.getId())){
//            throw new PodUserMappingNotFoundException();
//        }
        if (jjimRepository.existsByUserIdAndPodId(user.getId(), pod.getId())) {
            throw new DuplicateJJimException();
        }

        jjimRepository.save(new JJim(user, pod));
    }

    public List<JJimResponseDto> getJJimList(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        List<JJim> list = jjimRepository.findAllByUserId(user.getId());

        return list.stream()
                .map(jjim -> {
                    Pod pod = jjim.getPod();
                    return JJimResponseDto.builder()
                            .podId(pod.getId())
                            .podName(pod.getPodName())
                            .podType(pod.getPodType().name())
                            .platform(pod.getPlatform().name())
                            .goalAmount(pod.getGoalAmount())
                            .currentAmount(pod.getCurrentAmount())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void cancelJJim(Long podId, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());

//        if(!podUserMappingRepository.existsByPod_IdAndUser_Id(pod.getId(), user.getId())){
//            throw new PodUserMappingNotFoundException();
//        }
        if (jjimRepository.existsByUserIdAndPodId(user.getId(), pod.getId())) {
            jjimRepository.deleteByUserIdAndPodId(user.getId(), pod.getId());
        } else {
            throw new JJimNotFoundException();
        }
    }
}
