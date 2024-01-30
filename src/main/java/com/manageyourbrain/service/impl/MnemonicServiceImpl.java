package com.manageyourbrain.service.impl;

import com.manageyourbrain.domain.Mnemonic;
import com.manageyourbrain.repository.MnemonicRepository;
import com.manageyourbrain.service.MnemonicService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.manageyourbrain.domain.Mnemonic}.
 */
@Service
@Transactional
public class MnemonicServiceImpl implements MnemonicService {

    private final Logger log = LoggerFactory.getLogger(MnemonicServiceImpl.class);

    private final MnemonicRepository mnemonicRepository;

    public MnemonicServiceImpl(MnemonicRepository mnemonicRepository) {
        this.mnemonicRepository = mnemonicRepository;
    }

    @Override
    public Mnemonic save(Mnemonic mnemonic) {
        log.debug("Request to save Mnemonic : {}", mnemonic);
        return mnemonicRepository.save(mnemonic);
    }

    @Override
    public Mnemonic update(Mnemonic mnemonic) {
        log.debug("Request to update Mnemonic : {}", mnemonic);
        return mnemonicRepository.save(mnemonic);
    }

    @Override
    public Optional<Mnemonic> partialUpdate(Mnemonic mnemonic) {
        log.debug("Request to partially update Mnemonic : {}", mnemonic);

        return mnemonicRepository
            .findById(mnemonic.getId())
            .map(existingMnemonic -> {
                if (mnemonic.getName() != null) {
                    existingMnemonic.setName(mnemonic.getName());
                }
                if (mnemonic.getCreationDate() != null) {
                    existingMnemonic.setCreationDate(mnemonic.getCreationDate());
                }
                if (mnemonic.getModifiedDate() != null) {
                    existingMnemonic.setModifiedDate(mnemonic.getModifiedDate());
                }

                return existingMnemonic;
            })
            .map(mnemonicRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mnemonic> findAll() {
        log.debug("Request to get all Mnemonics");
        return mnemonicRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mnemonic> findOne(Long id) {
        log.debug("Request to get Mnemonic : {}", id);
        return mnemonicRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mnemonic : {}", id);
        mnemonicRepository.deleteById(id);
    }
}
