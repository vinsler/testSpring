package parts.services;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import parts.dao.DetailRepository;
import parts.entities.Detail;

import javax.validation.constraints.Null;
import java.util.List;

@Service // создали сервис класс Детали
public class DetailService {

    @Autowired // автозагрузили интерфейс JPA репозитория
    private DetailRepository detailRepository;

    public Integer findAllSize(){
        return detailRepository.findAll().size();
    }

    public List<Detail> findAll(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Detail> pageList = detailRepository.findAll(pageable);
        return  pageList.getContent();
    }

    public List<Detail> findByRequired(Integer page){
        Pageable pageable = PageRequest.of(page, 10);
        return detailRepository.findByRequired(pageable, true);
    }
    public Integer findByRequiredSize(){
        return detailRepository.findByRequired(true).size();
    }

    public List<Detail> findByOptional(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return detailRepository.findByRequired(pageable, false);
    }
    public Integer findByOptionalSize(){
        return detailRepository.findByRequired(false).size();
    }

    public Integer findMinimum(){
        return detailRepository.findMinimum();
    }

    public void delete(Integer id){
        detailRepository.deleteById(id);
    }

    public Detail findByName(String name){
        Detail detail = detailRepository.findByName(name);
        if (detail == null) {
            return new Detail();
        }
        return detail;
    }

    public void insert(Detail detail){
        detailRepository.saveAndFlush(detail);
    }


    public Model prewReturn(Model model, List<Detail> detailList, Integer page,
                             Integer size, String viewer){
        model.addAttribute(detailList);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("nameoflist", viewer);
        model.addAttribute("ctrlDetail", new Detail());
        return model;
    }

    public String creatRedirectForEditDelete(
            int dlist,
            String nameoflist,
            int page){
        StringBuffer sb = new StringBuffer().append("redirect:/");

        switch (nameoflist) {
            case "view":{
                sb.append("view?page=");
                break;
            }
            case "required":{
                sb.append("required?page=");
                break;
            }
            case "optional" :{
                sb.append("optional?page=");
                break;
            }
            case "find" :{
                return "redirect:/view?page=0";
            }
        }
        if (dlist == 1 && page > 0) {
            page -= page;
            sb.append(page);
            return sb.toString();
        }
        sb.append(page);
        return sb.toString();
    }
}
