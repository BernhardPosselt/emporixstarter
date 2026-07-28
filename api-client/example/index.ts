// example showing emporix api client in TS
// to run this file, define an OE_SECRET env variable, then execute this file using:
// ts-node index.ts

import {
    type ApiConfiguration,
    ApiError,
    EmporixHttpClient,
    HelloWorldEvent,
    OEClient,
    type OEConfig,
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
try {
    const result = await client.publish(new HelloWorldEvent(
        '3', {
            test: ''
        },
    ));
    console.log(result.body)

} catch (e) {
    if (e instanceof ApiError) {
        console.log(e.message)
    }
}
