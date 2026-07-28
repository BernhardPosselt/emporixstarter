// example showing emporix api client in TS
// to run this file, define an OE_SECRET env variable, then execute this file using:
// ts-node index.ts

import {
    type ApiConfiguration,
    EmporixHttpClient,
    HelloWorldEvent,
    OEClient,
    type OEConfig,
    OEResponse,
} from "../build/js/packages/emporix-api-client/kotlin/emporix-api-client.mjs"

const config: ApiConfiguration = {
    baseUrl: 'https://events.emporix.io/e/src_m74y2vqhlh66pp',
    tenant: 'fyaycff1'
}
declare var process: {
    env: {
        OE_SECRET: string
    }
}
const configOe: OEConfig = {
    baseUrl: 'https://events.emporix.io/e/src_m74y2vqhlh66pp',
    secret: process.env.OE_SECRET,
    source: 'nodejs test',
}
const http = new EmporixHttpClient();

const client = new OEClient(http, configOe)
const result = await client.publish(new HelloWorldEvent(
    '3', {
        test: ''
    },
));
if (result instanceof OEResponse.Ok) {
    console.log(result.body)
} else {
    console.log("error code " + result.statusCode)
}