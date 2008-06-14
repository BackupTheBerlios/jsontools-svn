          var jsonServletBatchedCalls = new Hash();

function jsonServletCallback (responderName, loadFx, dataToSend, batch)
{
    if(!Prototype)
    {
       throw('prototype.js has not been loaded and is required to make a json request');
    }
    if(!dataToSend)
    {
        dataToSend = {};
    }
    if (!batch)
    {
        batch = "GLOBAL";
    }
    if (!loadFx || !typeof loadFx == "function")
    {
        throw("You must provide a loadFx function parameter when making a json request");
    }
    var thisBatch = jsonServletBatchedCalls.get(batch);
    if (thisBatch == null || thisBatch.batchCount == 0)
    {
        jsonServletMakeSingleRequest(responderName, loadFx, dataToSend);
        return true
    }
    var callbackObj = {responderName:responderName, loadFx:loadFx, dataToSend:dataToSend}
    thisBatch.callArray.push(callbackObj);
    return true;
}
function jsonServletErrorHandler(request, exception)
{
    throw(exception);
}
function jsonServletStartBatch(batch)
{
    if (!batch)
    {
        batch = "GLOBAL";
    }
    var thisBatch = jsonServletBatchedCalls.get(batch);
    if (!thisBatch)
    {
        thisBatch = {batchCount:1, callArray:new Array()};
    }
    else
    {
        thisBatch.batchCount++;
    }
    jsonServletBatchedCalls.set(batch, thisBatch);
}
function jsonServletGetParams(dataToSend)
{
    var myHash = new Hash(dataToSend);
    myHash.set("noCache", Math.random() * 1000000000);
    var params = "";
    myHash.each(function (pair){
        if (typeof pair.value != "function")
        {
            params += "&"
            params += pair.key + "=" + encodeURIComponent(Object.toJSON(pair.value));
        }
    })
    return params
}
function jsonServletMakeSingleRequest(responderName, loadFx, dataToSend)
{
    var params = jsonServletGetParams(dataToSend);

    var successFunction = function (transport)
    {
        var jsonObj = transport.responseText.evalJSON(true)
        if(jsonServletLoadEvaluator(jsonObj))
        {
            loadFx(jsonObj, transport);
        }
    }
    var myAjax = new Ajax.Request(jsonServletDataUrl + responderName,{method: 'post',requestHeaders: ['json', 'true'],parameters: params,onComplete: successFunction, onException: jsonServletErrorHandler,onFailure: jsonServletErrorHandler});
}
function jsonServletMakeMultiRequest(batchObjs)
{
    var successFx = function (transport)
    {
        var jsonResultArray = transport.responseText.evalJSON(true);
        for (var i =0; i < jsonResultArray.length; i++)
        {
            var jsonObj = jsonResultArray[i];
            if (jsonServletLoadEvaluator(jsonObj))
            {
                batchObjs[i].loadFx(jsonObj);
            }
        }
    }
    var dataToSendArray = batchObjs.pluck("dataToSend");
    var responderNameArray = batchObjs.pluck("responderName");
    var params = jsonServletGetParams({dataToSendArray:dataToSendArray, responderNameArray: responderNameArray});
    var myAjax = new Ajax.Request(jsonServletDataUrl + "JSONServletBatchResponseHandler.json",{method: 'post',requestHeaders: ['json', 'true'],parameters: params,onComplete: successFx, onException: jsonServletErrorHandler,onFailure: jsonServletErrorHandler});
}
function jsonServletLoadEvaluator(jsonObj)
{
    return true;
}
function jsonServletExecuteBatch(batch)
{
    if (!batch)
    {
        batch = "GLOBAL";
    }
    var thisBatch = jsonServletBatchedCalls.get(batch);

    if (thisBatch == null)
    {
        return;
    }
    else if(thisBatch.batchCount > 0)
    {
        thisBatch.batchCount--;
    }
    if (thisBatch.callArray.length > 0)
    {
        jsonServletBatchedCalls.set(batch, null);
        if (thisBatch.callArray.length == 1)
        {
            var o = thisBatch.callArray[0];
            jsonServletMakeSingleRequest(o.responderName, o.loadFx, o.dataToSend);
        }
        else
        {
            jsonServletMakeMultiRequest(thisBatch.callArray);
        }
    }
}