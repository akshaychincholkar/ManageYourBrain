package com.manageyourbrain.domain;

import static com.manageyourbrain.domain.AppUserTestSamples.*;
import static com.manageyourbrain.domain.MemoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.manageyourbrain.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = getAppUserSample1();
        AppUser appUser2 = new AppUser();
        assertThat(appUser1).isNotEqualTo(appUser2);

        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);

        appUser2 = getAppUserSample2();
        assertThat(appUser1).isNotEqualTo(appUser2);
    }

    @Test
    void memoryTest() throws Exception {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Memory memoryBack = getMemoryRandomSampleGenerator();

        appUser.addMemory(memoryBack);
        assertThat(appUser.getMemories()).containsOnly(memoryBack);
        assertThat(memoryBack.getAppUser()).isEqualTo(appUser);

        appUser.removeMemory(memoryBack);
        assertThat(appUser.getMemories()).doesNotContain(memoryBack);
        assertThat(memoryBack.getAppUser()).isNull();

        appUser.memories(new HashSet<>(Set.of(memoryBack)));
        assertThat(appUser.getMemories()).containsOnly(memoryBack);
        assertThat(memoryBack.getAppUser()).isEqualTo(appUser);

        appUser.setMemories(new HashSet<>());
        assertThat(appUser.getMemories()).doesNotContain(memoryBack);
        assertThat(memoryBack.getAppUser()).isNull();
    }
}
