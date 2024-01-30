package com.manageyourbrain.domain;

import static com.manageyourbrain.domain.AppUserTestSamples.*;
import static com.manageyourbrain.domain.MemoryTestSamples.*;
import static com.manageyourbrain.domain.MnemonicTestSamples.*;
import static com.manageyourbrain.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.manageyourbrain.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Memory.class);
        Memory memory1 = getMemorySample1();
        Memory memory2 = new Memory();
        assertThat(memory1).isNotEqualTo(memory2);

        memory2.setId(memory1.getId());
        assertThat(memory1).isEqualTo(memory2);

        memory2 = getMemorySample2();
        assertThat(memory1).isNotEqualTo(memory2);
    }

    @Test
    void appUserTest() throws Exception {
        Memory memory = getMemoryRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        memory.setAppUser(appUserBack);
        assertThat(memory.getAppUser()).isEqualTo(appUserBack);

        memory.appUser(null);
        assertThat(memory.getAppUser()).isNull();
    }

    @Test
    void tagTest() throws Exception {
        Memory memory = getMemoryRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        memory.setTag(tagBack);
        assertThat(memory.getTag()).isEqualTo(tagBack);

        memory.tag(null);
        assertThat(memory.getTag()).isNull();
    }

    @Test
    void mnemonicTest() throws Exception {
        Memory memory = getMemoryRandomSampleGenerator();
        Mnemonic mnemonicBack = getMnemonicRandomSampleGenerator();

        memory.setMnemonic(mnemonicBack);
        assertThat(memory.getMnemonic()).isEqualTo(mnemonicBack);

        memory.mnemonic(null);
        assertThat(memory.getMnemonic()).isNull();
    }
}
