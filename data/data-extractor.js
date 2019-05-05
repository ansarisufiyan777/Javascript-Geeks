 var arr = [];
                        $(".im_message_text").each((i,ele)=>{
                            var obj = {
                                tags:[],
                                url:"",
                                caption:""
                            }
                            obj.caption = $(ele).html()
                            $(ele).find('a').each((i,v) => {if(i > 0) {
                                obj.tags.push(v.text)
                            }
                            else{
                                obj.url = v.text
                            }})
                            arr.push(obj)
                        })
                        copy(JSON.stringify(arr)) 